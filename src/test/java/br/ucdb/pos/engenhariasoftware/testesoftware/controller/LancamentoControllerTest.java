/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ucdb.pos.engenhariasoftware.testesoftware.controller;

import br.ucdb.pos.engenhariasoftware.testesoftware.converter.DateToStringConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.converter.MoneyToStringConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.converter.StringToMoneyConverter;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.Categoria;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.Lancamento;
import br.ucdb.pos.engenhariasoftware.testesoftware.modelo.TipoLancamento;
import static io.restassured.RestAssured.given;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Enilda Aparecida Mendes da Rosa Caceres
 */
public class LancamentoControllerTest {

    float valorMinimoCadastrado;

    @Test
    public void pingTestBasico() {
        given().when()
                .get("/lancamentos")
                .then()
                .statusCode(200);
    }

    @Test
    public void valorMininoNaListaDeTamanhoUmJava() {
        BigDecimal[] valores = new BigDecimal[1];
        valores[0] = new BigDecimal(50.50);

        limparBaseDeDados();
        geraLancamentosRandomicos(1, valores);

        InputStream in = getInputStreamDeLancamentosViaPost();

        List<Lancamento> lancamentos = JsonPath.with(in)
                .getList("lancamentos", Lancamento.class);

        BigDecimal menor = getMenorValorDaListaEmJava(lancamentos);
        assertEquals(menor.compareTo(valores[0]), 0);
    }

    @Test
    public void valorMininoNaListaDeTamanhoUmJsonPath() {
        BigDecimal[] valores = new BigDecimal[1];
        valores[0] = new BigDecimal(50.50);

        limparBaseDeDados();
        geraLancamentosRandomicos(1, valores);
        InputStream in = getInputStreamDeLancamentosViaPost();

        BigDecimal menor = getMenorValorDaListaJsonPath(in);

        assertEquals(menor.compareTo(valores[0]), 0);
    }

    @Test
    public void valorMininoNaListaDeTamanhoDoisJava() {
        BigDecimal[] valores = new BigDecimal[2];
        valores[0] = new BigDecimal(65.50);
        valores[1] = new BigDecimal(45.50);

        limparBaseDeDados();
        geraLancamentosRandomicos(2, valores);

        InputStream in = getInputStreamDeLancamentosViaPost();

        List<Lancamento> lancamentos = JsonPath.with(in)
                .getList("lancamentos", Lancamento.class);

        BigDecimal menor = getMenorValorDaListaEmJava(lancamentos);

        assertEquals(menor.compareTo(valores[1]), 0);
    }

    @Test
    public void valorMininoNaListaDeTamanhoDoisJsonPath() {
        BigDecimal[] valores = new BigDecimal[2];
        valores[0] = new BigDecimal(65.50);
        valores[1] = new BigDecimal(45.50);

        limparBaseDeDados();
        geraLancamentosRandomicos(2, valores);
        InputStream in = getInputStreamDeLancamentosViaPost();

        BigDecimal menor = getMenorValorDaListaJsonPath(in);
        System.out.println(menor);
        assertEquals(menor.compareTo(valores[1]), 0);
    }

    @Test
    public void valorMininoNaListaDeTamanhoMaiorQueCincoJava() {
        BigDecimal[] valores = new BigDecimal[6];
        valores[0] = new BigDecimal(65.50);
        valores[1] = new BigDecimal(700.50);
        valores[2] = new BigDecimal(500.55);
        valores[3] = new BigDecimal(10.50);
        valores[4] = new BigDecimal(10.50);
        valores[5] = new BigDecimal(45.50);

        limparBaseDeDados();
        geraLancamentosRandomicos(6, valores);

        InputStream in = getInputStreamDeLancamentosViaPost();

        List<Lancamento> lancamentos = JsonPath.with(in)
                .getList("lancamentos", Lancamento.class);

        BigDecimal menor = getMenorValorDaListaEmJava(lancamentos);
        assertEquals(menor.compareTo(valores[3]), 0);

    }

    @Test
    public void valorMininoNaListaDeTamanhoMaiorQueCincoJsonPath() {
        BigDecimal[] valores = new BigDecimal[6];
        valores[0] = new BigDecimal(65.50);
        valores[1] = new BigDecimal(700.50);
        valores[2] = new BigDecimal(500.55);
        valores[3] = new BigDecimal(10.50);
        valores[4] = new BigDecimal(10.50);
        valores[5] = new BigDecimal(45.50);

        limparBaseDeDados();
        geraLancamentosRandomicos(6, valores);
        InputStream in = getInputStreamDeLancamentosViaPost();

        BigDecimal menor = getMenorValorDaListaJsonPath(in);
        System.out.println(menor);
        assertEquals(menor.compareTo(valores[3]), 0);
    }

    /**
     * ***************************CONCLUSÃO************************************
     *
     *
     *
     **************************************************************************
     */
    public BigDecimal getMenorValorDaListaEmJava(List<Lancamento> lancamentos) {
        BigDecimal menor = lancamentos.get(0).getValor();

        for (Lancamento lancamento : lancamentos) {
            if (lancamento.getValor().compareTo(menor) == -1) {
                menor = lancamento.getValor();
            }
        }
        return menor;
    }

    public BigDecimal getMenorValorDaListaJsonPath(InputStream in) {
        String valor = JsonPath.with(in).getString("lancamentos.min{it.valor}.valor");
        double dinheiro = new StringToMoneyConverter().convert(valor).doubleValue();
        return new BigDecimal(dinheiro);
    }

    /*
    *   Funcao utilizada para garantir que a base de dados está vazia
     */
    public void limparBaseDeDados() {
        Response response = given()
                .when()
                .body("Teste Enilda")
                .post("/buscaLancamentos");
        InputStream in = response.asInputStream();
        assertEquals(response.getStatusCode(), 200);
        List<Lancamento> lancamentos = JsonPath.with(in).getList("lancamentos", Lancamento.class);
        for (Lancamento lancamento : lancamentos) {
            response = given().pathParam("id", lancamento.getId()).when().get("/remover/{id}");
        }
        response = given()
                .when()
                .body("Teste Enilda")
                .post("/buscaLancamentos");
        in = response.asInputStream();
        lancamentos = JsonPath.with(in).getList("lancamentos", Lancamento.class);
        assertEquals(lancamentos.size(), 0);
    }

    /*
    *   Gera Lancamentos Randomicos e POST na API
    *   @param Quantidade de registros que deseja gerar
    *   @param Vetor com os valores à ser postado
     */
    public void geraLancamentosRandomicos(int quantidade, BigDecimal[] valor) {
        Calendar calendar = Calendar.getInstance();
        SecureRandom random = new SecureRandom();
        Random rand = new Random();
        Lancamento lancamento = new Lancamento();

        //  Cria um lançamentos randomicos para realização do testes
        for (int i = 0; i < quantidade; i++) {
            // Cria o Objeto
            lancamento.setTipoLancamento(rand.nextBoolean() ? TipoLancamento.ENTRADA : TipoLancamento.SAIDA);
            lancamento.setDescricao("Teste Enilda");
            calendar.set(2018, 9, rand.nextInt(8));
            lancamento.setDataLancamento(calendar.getTime());
            lancamento.setValor(valor[i]);
            int x = random.nextInt(Categoria.class.getEnumConstants().length);
            lancamento.setCategoria(Categoria.class.getEnumConstants()[x]);
            postLancamento(lancamento);
        }

    }

    /*   
    *   Posta via requisição  REST
    *   @param Lista de 
     */
    public void postLancamento(Lancamento lancamento) {
        Response response = given().when()
                .formParam("descricao", lancamento.getDescricao())
                .formParam("valor", new MoneyToStringConverter().convert(lancamento.getValor()))
                .formParam("dataLancamento", new DateToStringConverter().convert(lancamento.getDataLancamento()))
                .formParam("tipoLancamento", lancamento.getTipoLancamento())
                .formParam("categoria", lancamento.getCategoria())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post("/salvar");
        assertEquals(response.getStatusCode(), 302);
    }

    public InputStream getInputStreamDeLancamentosViaPost() {
        Response response = given()
                .when()
                .body("Teste Enilda")
                .post("/buscaLancamentos");

        assertEquals(response.getStatusCode(), 200);

        return response.asInputStream();
    }

}
