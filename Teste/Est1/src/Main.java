import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;



public class Main {
    public static void main(String[] args) {
        System.out.println("O código está rodando!");
    }

    String[] arquivosurl = {
            "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos/Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf",
            "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos/Anexo_II_DUT_2021_RN_465.2021_RN628.2025_RN629.2025.pdf"
    };
    String[] nomesdosarquivosurl = {"anexo-i.pdf", "anexo-ii.pdf"};
    String downloadDiretorio = "Downloads";
    String nomeZip = "Anexos.zip";



    {
        try {
            Files.createDirectories(Paths.get(downloadDiretorio));
            for (int i = 0; i < arquivosurl.length; i++) {
                downloadFile(arquivosurl[i], downloadDiretorio + "/" + nomesdosarquivosurl[i]);
            }
            zipFiles(nomesdosarquivosurl, downloadDiretorio, nomeZip);
            System.out.println("Arquivos criados com sucesso!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }








}

private static void downloadFile(String arquivourl, String destino) throws IOException {
    try(InputStream in = new URL(arquivourl).openStream()) {
        Files.copy(in, Paths.get(destino), StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Arquivo salo em: " + destino);
    }
}

    private static void zipFiles(String[] nomesArquivos, String diretorio, String nomeZip) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(diretorio + "/" + nomeZip);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String nomeArquivo : nomesArquivos) {
                File fileToZip = new File(diretorio + "/" + nomeArquivo);
                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(nomeArquivo);
                    zos.putNextEntry(zipEntry);

                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                    System.out.println("Adicionado ao ZIP: " + nomeArquivo);
                }
            }
        }
    }
}