import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVWriter;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import tabula.*;

public class ExtrairTabelaPDF {
    public static void main(String[] args) {
        String nomeUsuario = "Luis Filipe da Silva Periard";
        String urlPDF = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos/Anexo_I_Rol_2021RN_465.2021_RN627L.2024.pdf";
        String pdfFileName = "anexo-i.pdf";
        String csvFileName = "rol_de_procedimentos.csv";
        String zipFileName = "Teste_" + nomeUsuario + ".zip";
        String downloadDir = "Downloads";

        try {
            // Configuração inicial
            Files.createDirectories(Paths.get(downloadDir));
            baixarArquivo(urlPDF, downloadDir + "/" + pdfFileName);

            // Extrair tabelas usando Tabula
            extrairTabelaParaCSV(downloadDir + "/" + pdfFileName, downloadDir + "/" + csvFileName);

            // Compactar CSV
            compactarEmZip(downloadDir + "/" + csvFileName, downloadDir + "/" + zipFileName);

            System.out.println("Processo concluído! Arquivo ZIP criado: " + downloadDir + "/" + zipFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para baixar arquivo com verificação
    private static void baixarArquivo(String fileUrl, String destino) throws IOException {
        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, Paths.get(destino), StandardCopyOption.REPLACE_EXISTING);

            // Verificar integridade do download
            Path destinoPath = Paths.get(destino);
            if(Files.size(destinoPath) == 0) {
                throw new IOException("Download falhou: arquivo vazio");
            }

            System.out.println("Arquivo baixado: " + destino);
        }
    }

    // Método para extrair tabela usando Tabula
    private static void extrairTabelaParaCSV(String pdfFilePath, String csvFilePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(
                new FileWriterWithEncoding(csvFilePath, "UTF-8"),
                CSVFormat.DEFAULT
                        .withHeader("Código", "Descrição", "OD", "AMB")
                        .withQuoteMode(QuoteMode.ALL)
                        .withDelimiter(';')
        )) {

            // Configurar extrator de tabelas
            TabulaExtractor extractor = new TabulaExtractor();
            extractor.setGuessOptions(true);
            extractor.setPages("all");

            // Extrair e processar tabelas
            List<Table> tabelas = extractor.extract(pdfFilePath);
            tabelas.forEach(tabela -> {
                tabela.writeToCSV(writer);
                System.out.println("Tabela processada: " + tabela.getRowCount() + " linhas");
            });

            System.out.println("Arquivo CSV criado: " + csvFilePath);
        }
    }

    // Método para compactar arquivo
    private static void compactarEmZip(String csvFilePath, String zipFilePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            // Adicionar arquivo ao ZIP
            ZipEntry entry = new ZipEntry(new File(csvFilePath).getName());
            zos.putNextEntry(entry);

            // Copiar conteúdo usando buffer
            Files.copy(Paths.get(csvFilePath), zos);

            System.out.println("Arquivo compactado: " + zipFilePath);
        }
    }
}
