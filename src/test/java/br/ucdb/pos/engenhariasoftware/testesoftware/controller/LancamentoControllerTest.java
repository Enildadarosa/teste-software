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
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * *****************************************************************************
 *
 * @author Enilda Aparecida Mendes da Rosa Caceres
 *
 * *******************************INSTRUÇÕES************************************
 *
 * Foram gerados sete métodos para realização dos testes descritos. Para cada
 * caso de teste descrito, foi implementado um método em Java Nativo e outro
 * utilizando a classe JsonPath.
 *
 * Foi definido um vetor com os valores que serão utilizados nos assert do
 * métodos de teste. Esse vetor é inicializado antes do inicio da execução dos
 * testes, no método init.
 *
 * Para a realização dos testes, não é necessário cadastrar manualemnte os dados
 * na API, para isso, o método geraLancamentosRandomicos é executado em cada
 * caso de teste com os valore informados no vetor.
 *
 * Caso desejar usar a base já cadastrada, comentar os métodos:
 * limparBaseDeDados(); geraLancamentosRandomicos();
 *
 * e adequar os parâmetros dos asserts
 *
 * ***************************CONCLUSÃO*****************************************
 *
 * Nos casos de testes implementados em Java, ao realizar a comparação entre
 * valores com duas casas decimais (850.52) percebe-se que o BigDecimal possui
 * dízima periódica (850.51999999999998181010596454143524169921875) dessa forma
 * o caso de teste falha durante a execução.
 *
 * Além do mais, no Java é necessário codificar o método que realiza a tarefa,
 * enquanto que utilizando o JsonPath, essa tarefa fica a cargo da classe.
 *
 * Dessa forma, recomenda-se a utilização do JsonPath para esse tipo de teste.
 *
 * *****************************************************************************
 */
public class LancamentoControllerTest {

    BigDecimal[] valores = new BigDecimal[6];

    public static final int SIZE_ONE = 1;
    public static final int SIZE_TWO = 2;
    public static final int SIZE_SIX = 6;

    @BeforeTest
    public void init() {
        valores[0] = new BigDecimal(850.50);  // Configurado no assert para ser o menor valor da lista = 1
        valores[1] = new BigDecimal(700.50);  // Configurado no assert para ser o menor valor da lista = 2
        valores[2] = new BigDecimal(500.60);
        valores[3] = new BigDecimal(500.34);  // Configurado no assert para ser o menor valor da lista > 5
        valores[4] = new BigDecimal(500.50);
        valores[5] = new BigDecimal(900.70);
    }

    /**
     * Verifica a conexão com o servidor
     */
    @Test
    public void pingTestBasico() {
        given().when()
                .get("/lancamentos")
                .then()
                .statusCode(200);
    }

    /**
     * CASO DE TESTE 01 - JAVA
     *
     * Encontrar o menor valor da lista de tamanho UM usando o cógido Java
     */
    @Test
    public void valorMininoNaListaDeTamanhoUmJava() {

        limparBaseDeDados();
        geraLancamentosRandomicos(SIZE_ONE, valores);

        InputStream in = getInputStreamDeLancamentosViaPost();

        List<Lancamento> lancamentos = JsonPath.with(in)
                .getList("lancamentos", Lancamento.class);

        BigDecimal menor = getMenorValorDaListaEmJava(lancamentos);
        System.out.println(menor);
        System.out.println(valores[0]);
        assertEquals(menor.compareTo(valores[0]), 0);
    }

    /**
     * CASO DE TESTE 01 - JsonPath
     *
     * Encontrar o menor valor da lista de tamanho UM usando o jsonPath
     */
    @Test
    public void valorMininoNaListaDeTamanhoUmJsonPath() {

        limparBaseDeDados();
        geraLancamentosRandomicos(SIZE_ONE, valores);
        InputStream in = getInputStreamDeLancamentosViaPost();

        BigDecimal menor = getMenorValorDaListaJsonPath(in);
        System.out.println(menor);
        System.out.println(valores[0]);
        assertEquals(menor.compareTo(valores[0]), 0);
    }

    /**
     * CASO DE TESTE 02 - JAVA
     *
     * Encontrar o menor valor da lista de tamanho DOIS usando JAVA
     */
    @Test
    public void valorMininoNaListaDeTamanhoDoisJava() {

        limparBaseDeDados();
        geraLancamentosRandomicos(SIZE_TWO, valores);

        InputStream in = getInputStreamDeLancamentosViaPost();

        List<Lancamento> lancamentos = JsonPath.with(in)
                .getList("lancamentos", Lancamento.class);

        BigDecimal menor = getMenorValorDaListaEmJava(lancamentos);

        assertEquals(menor.compareTo(valores[1]), 0);
    }

    /**
     * CASO DE TESTE 02 - JsonPath
     *
     * Encontrar o menor valor da lista de tamanho DOIS usando JsonPath
     */
    @Test
    public void valorMininoNaListaDeTamanhoDoisJsonPath() {

        limparBaseDeDados();
        geraLancamentosRandomicos(SIZE_TWO, valores);
        InputStream in = getInputStreamDeLancamentosViaPost();

        BigDecimal menor = getMenorValorDaListaJsonPath(in);
        //System.out.println("Aqui" + menor);
        assertEquals(menor.compareTo(valores[1]), 0);
    }

    /**
     * CASO DE TESTE 03 - JAVA
     *
     * Encontrar o menor valor da lista de tamanho MAIOR QUE CINCO usando JAVA
     */
    @Test
    public void valorMininoNaListaDeTamanhoMaiorQueCincoJava() {
        limparBaseDeDados();
        geraLancamentosRandomicos(SIZE_SIX, valores);

        InputStream in = getInputStreamDeLancamentosViaPost();

        List<Lancamento> lancamentos = JsonPath.with(in)
                .getList("lancamentos", Lancamento.class);

        BigDecimal menor = getMenorValorDaListaEmJava(lancamentos);
        assertEquals(menor.compareTo(valores[3]), 0);

    }

    /**
     * CASO DE TESTE 03 - JsonPath
     *
     * Encontrar o menor valor da lista de tamanho MAIOR QUE CINCO usando
     * JsonPath
     */
    @Test
    public void valorMininoNaListaDeTamanhoMaiorQueCincoJsonPath() {

        limparBaseDeDados();
        geraLancamentosRandomicos(SIZE_SIX, valores);
        InputStream in = getInputStreamDeLancamentosViaPost();

        BigDecimal menor = getMenorValorDaListaJsonPath(in);
        //System.out.println(menor);
        assertEquals(menor.compareTo(valores[3]), 0);
    }

    /**
     * Método que retorna o menor valor de uma lista de lançamentos
     *
     * @param Lista de lançamentos
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

    /**
     * Método que retorna o menor valor de uma lista utilizando a classe
     * JsonPath
     *
     * @param InputStream recebido da requisição
     */
    public BigDecimal getMenorValorDaListaJsonPath(InputStream in) {
        String valor = JsonPath.with(in).getString("lancamentos.min{it.valor}.valor");
        double dinheiro = new StringToMoneyConverter().convert(valor).doubleValue();
        return new BigDecimal(dinheiro);
    }

    /**
     * Função utilizada para garantir que a base de dados está vazia
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
    *   @param Lista de lancamentos
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

    /**
     * Função utilizada para fazer uma requisição via POST na API, receber a
     * resposta e retornar um InputStream como resultado
     */
    public InputStream getInputStreamDeLancamentosViaPost() {
        Response response = given()
                .when()
                .body("Teste Enilda")
                .post("/buscaLancamentos");

        assertEquals(response.getStatusCode(), 200);

        return response.asInputStream();
    }

}
