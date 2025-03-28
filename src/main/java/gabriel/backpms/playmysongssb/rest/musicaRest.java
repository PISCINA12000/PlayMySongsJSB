package gabriel.backpms.playmysongssb.rest;

import gabriel.backpms.playmysongssb.entities.Erro;
import gabriel.backpms.playmysongssb.entities.Musica;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("apis/musicas")
public class musicaRest {
    @Autowired
    private HttpServletRequest request;

    private static final String UPLOAD_FOLDER = "src/main/resources/static/uploads/";

    // Esse metodo ira gravar uma musica na pasta de destino
    @PostMapping("gravar")
    public ResponseEntity<Object> gravarMusica(@RequestParam("arquivo") MultipartFile arquivo,
                                               @RequestParam("nome") String nome,
                                               @RequestParam("cantor") String cantor,
                                               @RequestParam("estilo") String estilo) throws IOException {
        File destino;
        // Nome do arquivo formatado e pronto para salvar
        String nomeFormatado = nome + "_" + estilo + "_" + cantor + ".mp3";
        nomeFormatado = nomeFormatado.replaceAll(" ", "");

        try {
            //cria uma pasta na área estática para acomodar os arquivos recebidos, caso não exista
            File uploadFolder = new File(UPLOAD_FOLDER);
            if (!uploadFolder.exists())
                uploadFolder.mkdir();

            //criar um nome novo para o arquivo
            arquivo.transferTo(new File(uploadFolder.getAbsolutePath() + "/" + nomeFormatado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Erro("Erro ao armazenar o arquivo. " + e.getMessage()));
        }
        return ResponseEntity.ok(new Musica(nome, estilo, cantor, arquivo.getOriginalFilename()));
    }

    // Esse metodo busca apenas apenas as musicas que se encontram no filtro
    @GetMapping("buscar/{filtro}")
    public ResponseEntity<Object> buscarMusicas (@PathVariable(value = "filtro") String filtro){
        List<Musica> musicas = new ArrayList<>();
        File uploadFolder = new File(UPLOAD_FOLDER);

        // Possuo a lista dos nomes dos arquivos nessa minha pasta
        String[] fileNames = uploadFolder.list();
        if(fileNames.length == 0){ //nenhum arquivo na minha pasta
            return ResponseEntity.badRequest().body(new Erro("Não há nenhuma música CADASTRADA!!"));
        }
        else{ //possui músicas para serem buscadas
            for (String fileName : fileNames) {
                if(fileName.toLowerCase().contains(filtro.toLowerCase()) && fileName.endsWith(".mp3")){ //para ter certeza que é um arquivo .mp3
                    String nomeArquivo = fileName.replace(".mp3", "");
                    String[] partes = nomeArquivo.split("_");
                    musicas.add(new Musica(partes[0], partes[1], partes[2], getHostStatic()+fileName));
                }
            }
            if(musicas.isEmpty()){ //nenhuma música correspondeu com o filtro
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Erro("Nenhuma música com o filtro " + filtro));
            }
        }
        return ResponseEntity.ok().body(musicas);
    }

    // Esse metodo ira retornar todas as musicas que estiverem gravadas na pasta
    @GetMapping("buscar")
    public ResponseEntity<Object> buscarMusicas () {
        List<Musica> musicas = new ArrayList<>();
        File uploadFolder = new File(UPLOAD_FOLDER);

        // Possuo a lista dos nomes dos arquivos nessa minha pasta
        String[] fileNames = uploadFolder.list();
        if(fileNames.length == 0){ //nenhum arquivo na minha pasta
            return ResponseEntity.badRequest().body(new Erro("Não há nenhuma música CADASTRADA!!"));
        }
        else{ //possui músicas para serem retornadas
            for (String fileName : fileNames) {
                if(fileName.endsWith(".mp3")) {
                    String nomeArquivo = fileName.replace(".mp3", "");
                    String[] partes = nomeArquivo.split("_");
                    musicas.add(new Musica(partes[0], partes[1], partes[2], getHostStatic() + fileName));
                }
            }
            if(musicas.isEmpty()){ //nenhuma música correspondeu com o filtro
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Erro("Nenhuma música cadastrada ainda!!"));
            }
        }
        return ResponseEntity.ok().body(musicas);
    }

    public String getHostStatic() {
        return "http://"+request.getServerName().toString()+":"+request.getServerPort()+ "/uploads/";
    }
}
