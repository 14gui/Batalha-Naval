import java.io.*

const val MENU_PRINCIPAL = 100
const val MENU_DEFINIR_TABULEIRO = 101
const val MENU_DEFINIR_NAVIOS = 102
const val MENU_JOGAR = 103
const val MENU_LER_FICHEIRO = 104
const val MENU_GRAVAR_FICHEIRO = 105
const val SAIR = 106
const val MENSAGEM_POR_IMPLEMENTAR = "!!! Tem que primeiro defenir o tabuleiro de jogo, tente novamente"
// colocar uma coisa numa slot do tabuleiro = tabuleiro[linha][coluna] = há coisa queres inserir IMPORTANTE SABER !!!
// verificar se existe alguma coisa dentro do slot = tabuleiro[linha][coluna] == há coisa que queres virificar que existe = Boolean false or true
// exemplo : mete na posiçao (2,3) um "Y"/ Resposta tabuleiro[2][3] = 'Y'
// 4 tabuleiros começam vazios sem nada quadrado gigante sem nada
var tabuleiroHumano: Array<Array<Char?>> = emptyArray()

var tabuleiroComputador: Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()

fun calculaNaviosFaltaAfundar(tabuleiroPalpites: Array<Array<Char?>>): Array<Int> {


    val naviostotal = calculaNumNavios(tabuleiroPalpites.size, tabuleiroPalpites[0].size)
    val naviosemfalta = arrayOf(0, 0, 0, 0)
    val submarino = naviostotal[0]
    val contrator = naviostotal[1]
    val naviotank = naviostotal[2]
    val navioaviao = naviostotal[3]
    val submariocomp = contarNaviosDeDimensao(tabuleiroPalpites, 1)
    val contropedeirocomp = contarNaviosDeDimensao(tabuleiroPalpites, 2)
    val naviotankcomp = contarNaviosDeDimensao(tabuleiroPalpites, 3)
    val navioaviaocomp = contarNaviosDeDimensao(tabuleiroPalpites, 4)

    naviosemfalta[3] = submarino - submariocomp
    naviosemfalta[2] = contrator - contropedeirocomp
    naviosemfalta[1] = naviotank - naviotankcomp
    naviosemfalta[0] = navioaviao - navioaviaocomp

    return naviosemfalta
}

fun calculaEstatisticas(tabuleiroPalpites: Array<Array<Char?>>): Array<Int> {
    val arrayIndicador = arrayOf(0, 0, 0)
    var numeroDeJogadas = 0
    var tirosfalhados = 0
    var numeroDeNaviosAfundados = 0
    for (linha in 0 until tabuleiroPalpites.size) {
        for (coluna in 0 until tabuleiroPalpites[linha].size) {
            if (tabuleiroPalpites[linha][coluna] != null) {
                numeroDeJogadas++
                if (tabuleiroPalpites[linha][coluna] == 'X') {
                    tirosfalhados++
                }
            }
        }
    }
    val numeroDeTirosCerteiros = numeroDeJogadas - tirosfalhados
    numeroDeNaviosAfundados += contarNaviosDeDimensao(tabuleiroPalpites,1)
    numeroDeNaviosAfundados += contarNaviosDeDimensao(tabuleiroPalpites,2)
    numeroDeNaviosAfundados += contarNaviosDeDimensao(tabuleiroPalpites,3)
    numeroDeNaviosAfundados += contarNaviosDeDimensao(tabuleiroPalpites,4)
    arrayIndicador[0] = numeroDeJogadas
    arrayIndicador[1] = numeroDeTirosCerteiros
    arrayIndicador[2] = numeroDeNaviosAfundados
    return arrayIndicador
}

// pega no numero de linhas e de colunas e vai ver se o tamanho é valido e retorna true se for valido o programa roda se for falso
fun tamanhoTabuleiroValido(numLinhas: Int, numColunas: Int): Boolean {
    return when {
        numLinhas == 4 && numColunas == 4 -> true
        numLinhas == 5 && numColunas == 5 -> true
        numLinhas == 7 && numColunas == 7 -> true
        numLinhas == 8 && numColunas == 8 -> true
        numLinhas == 10 && numColunas == 10 -> true
        else -> false
    }
}

fun letraParaNumero(letra: Char): Int {
    return letra.toUpperCase() - 'A' + 1

}
// recebe as coordenadas que o utilizador introduziu e as linhas e colunas que ele tambem introduziu para defenir o tamanho do tabuleiro
fun processaCoordenadas(coordenadas: String, numLinhas: Int, numColunas: Int): Pair<Int, Int>? {
    val partes = coordenadas.split(",")
    // var partes é um array que divide pela a virgola e o que tiver antes da virgola fica na primera posição e depois o resto na segunda posição
    // por exemplo : cord"1,A" igual partes[0] = 1 e partes[1] = A
    if (coordenadas.length < 3) {//tamanho das coordenadas for menor que 3 retorna null exemplo : 1A porque o tamnho disso é 2 e tem de ser sempre 3 , 1,A
        return null
    }
    // val num é igual há primeira posição do partes que deve ser um numero (linha) , se não for exemplo A,2 retorna null porque não é uma coordenada válida
    val num = partes[0].toIntOrNull() ?: return null
    // se o numero que o utilizador introduzir for menor que ou igual que numero de linhas do tabuleiro é valido se não retorna null
    if (num <= numLinhas) {
        val letra = partes[1]//letra que o utlizador introduziu
        if (letra[0].isUpperCase() && letra[0] in 'A'..'Z') {// verifica se a letra é maiuscula (uperCase) e se pertence ao abcedario A..Z
            val letra2 = letra[0].toInt() - 'A'.toInt() + 1 // val letra2 está a passar a letra que o ultizador introduziu para um numero
            if (letra2 in 1..numColunas) {// se a letra não tiver dentro da tabuleiro
                return Pair(num, letraParaNumero(letra[0]))// retorna o pair consoante as coordenadas que o utlizador pediu exemplo : 1,A pair(1,1)
            }
            return null
        }
        return null
    }
    return null
}
// vais receber  o numero de linhas e dependendo do tabuleiro limete o numero de barcos
// array posição 1 = submarino
// array posição 2 = torpedo
// array posição 3 = tanque
// array posição 4 = avião
fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int> {
    return when {
        numLinhas == 4 && numColunas == 4 -> arrayOf(2, 0, 0, 0)
        numLinhas == 5 && numColunas == 5 -> arrayOf(1, 1, 1, 0)
        numLinhas == 7 && numColunas == 7 -> arrayOf(2, 1, 1, 1)
        numLinhas == 8 && numColunas == 8 -> arrayOf(2, 2, 1, 1)
        numLinhas == 10 && numColunas == 10 -> arrayOf(3, 2, 1, 1)
        else -> arrayOf()
    }
}
// A primeira linha do tabuleiro A | B | C | D , sem as barras no inicio e sem no fim
fun criaLegendaHorizontal(numColunas: Int): String {
    var letra = 'A'// letra incial
    var count = 0 // conta as vezes que vais contruindo a string
    var seq = "" // string completa começa vazia e acaba como lá em cima
    while (count != numColunas) {// enquanto o count for difente do numero de colunas do tabuleiro vai acrescentar há seq a coluna
        seq += letra // adiciona-se a letra a seq exemplo : seq = "A"
        letra++ // var letra vai se igual a B
        count++ // aumenta o numero o numero de colunas que meti tinha o A para a proxima fica com o B count = 2
        if (count != numColunas && count > 0) { // enquanto o count não for igual ao numero de colunas vai ser sempre acrescentar uma barra "A|"
            seq += " | " // acrescenta uma barra há seq sequencia estava a "A" e agora está a "A | "
        }
    }
    return seq // sequencia toda completa
}

fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int): Array<Array<Char?>> {
    return Array(numLinhas) { Array(numColunas) { null } } // cria a o tabuleiro de acordo com o que o utitlizador introduzir
}
fun coordenadaContida(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {// tabuleiro é um array com colunas e linhas
    return coluna <= tabuleiro[0].size && coluna > 0 && linha <= tabuleiro.size && linha > 0 //tamanho da coluna = tabuleiro[0].size, tamanho da linha = tabuleiro.size
}// se a coordenada estiver dentro do tabulero retorna true se não retorna false

fun limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    val novoArray = coordenadas.filter { it != Pair(0, 0) }.toTypedArray()//tira todos os pair(0,0) de uma array de pairs
    return Array(novoArray.size) { novoArray[it] }
}
fun juntarCoordenadas(array1: Array<Pair<Int, Int>>, array2: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    return array1 + array2// o array1 = pair(1,0) array2 = pair(0,1) juntaCoordenadas retorna uma array3 = (pair(1,0),pair (0,1))
}
// gera um array com as coordenadas onde o navio vai ser colocado ( NÃO METE O NAVIO )
fun gerarCoordenadasNavio(
    tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int
): Array<Pair<Int, Int>> {// o array esta com tamanho 0
    val coordenadasNavio = Array(dimensao) { Pair(0, 0) }//o array fica com o tamanho da dimensao do barco e fica pair(0,0) todas as posições

    for (posicaoIncial in 0 until dimensao) {// o for para quando chegar há dimensao para de gerar as coordenadas
        val novaLinha = when (orientacao) {
            "N" -> linha - posicaoIncial // "N" CIMA linha -
            "S" -> linha + posicaoIncial // "S" BAIXO linha +
            else -> linha
        }
        val novaColuna = when (orientacao) {
            "E" -> coluna + posicaoIncial // "E" DIREITA coluna +
            "O" -> coluna - posicaoIncial // "O" ESQUERDA coluna -
            else -> coluna
        }
        if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {// vai ver se a novaCoordenada vai estar dentro do tabuleiro
            coordenadasNavio[posicaoIncial] = Pair(novaLinha, novaColuna)// coordenadas do meu navio por ordem da posicçãoinicial
        } else {

            return emptyArray()// se alguma coordenada não tiver contida no tabuleiro retorna um array vazio
        }
    }

    return coordenadasNavio // retorno as coordenadas validas onde vou colocar o navio
}

fun gerarCoordenadasFronteira(
    tabuleiro: Array<Array<Char?>>,
    linha: Int,
    coluna: Int,
    orientacao: String,
    dimensao: Int
): Array<Pair<Int, Int>> {
    var tamanhoArray = 8
    // Ajusta o tamanho com base na dimensao
    if (dimensao > 1) {
        tamanhoArray += (dimensao - 1) * 2
    }
    val coordenadasNavio = Array(tamanhoArray) { Pair(0, 0) }
    var novaLinha = linha
    var novaColuna = coluna
    when (orientacao) {
        "S" -> {
            var gravaColuna = 0
            // parte de cima 3
            novaLinha--
            novaColuna--
            for (posicaoInicial in 0 until 3) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaColuna++
            }

            // parte de baixo 3
            novaLinha += dimensao
            novaLinha++
            novaColuna--
            for (posicaoInicial in 3 until 6) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaColuna--
            }

            // parte esquerda
            novaLinha--
            novaColuna++
            for (posicaoInicial in 6 until 6 + dimensao) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaLinha--
                gravaColuna = posicaoInicial
            }

            // parte direita
            novaColuna += 2
            novaLinha++
            gravaColuna++
            for (posicaoInicial in gravaColuna until gravaColuna + dimensao) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaLinha++
            }
        }

        "N" -> {
            var gravaColuna = 0

            // parte de cima 3
            novaLinha++
            novaColuna++
            for (posicaoInicial in 0 until 3) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaColuna--
            }

            // parte de baixo 3
            novaLinha -= dimensao
            novaLinha--
            novaColuna++
            for (posicaoInicial in 3 until 6) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaColuna++
            }

            // parte direita
            novaLinha++
            novaColuna--
            for (posicaoInicial in 6 until 6 + dimensao) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaLinha++
                gravaColuna = posicaoInicial
            }

            // parte esquerda
            gravaColuna++
            novaColuna -= 2
            novaLinha--
            for (posicaoInicial in gravaColuna until gravaColuna + dimensao) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaLinha--
            }
        }

        "E" -> {
            var gravaColuna = 0

            // parte de esquerda 3
            novaLinha--
            novaColuna--
            for (posicaoInicial in 0 until 3) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaLinha++
            }

            // parte da direita 3
            novaColuna += dimensao
            novaColuna++
            novaLinha--
            for (posicaoInicial in 3 until 6) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaLinha--
            }

            // parte cima
            novaLinha++
            novaColuna--
            for (posicaoInicial in 6 until 6 + dimensao) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaColuna--
                gravaColuna = posicaoInicial
            }

            // parte baixo
            novaLinha += 2
            novaColuna++
            gravaColuna++
            for (posicaoInicial in gravaColuna until gravaColuna + dimensao) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaColuna++
            }
        }

        "O" -> {
            var gravaColuna = 0

            // parte de direita 3
            novaLinha-- // aceder há parte de cima
            novaColuna++ // muda para a esquerda a coluna
            for (posicaoInicial in 0 until 3) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaLinha++
            }

            // parte de esquerda 3
            novaColuna -= dimensao
            novaColuna--
            novaLinha-- // diminui uma linha a mais
            for (posicaoInicial in 3 until 6) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaLinha--
            }

            // parte cima
            novaColuna++
            novaLinha++
            for (posicaoInicial in 6 until 6 + dimensao) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaColuna++
                gravaColuna = posicaoInicial
            }

            // parte baixo
            novaColuna--
            gravaColuna++
            novaLinha += 2
            for (posicaoInicial in gravaColuna until gravaColuna + dimensao) {
                if (coordenadaContida(tabuleiro, novaLinha, novaColuna)) {
                    coordenadasNavio[posicaoInicial] = Pair(novaLinha, novaColuna)
                } else {
                    coordenadasNavio[posicaoInicial] = Pair(0, 0)
                }
                novaColuna--
            }
        }
    }
    val coordenadasLimpas = limparCoordenadasVazias(coordenadasNavio)
    return coordenadasLimpas
}
// recebe as coordenadas quem vem do geraNavio
fun estaLivre(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>): Boolean { // recebo um array com as coordenadas e vai percorrer o array linha e coluna de uma vez
    for ((linha, coluna) in coordenadas) {
        // Verifica se as coordenadas estão dentro dos limites do tabuleiro       (1,3) (1,5)
        if (coordenadaContida(tabuleiro, linha, coluna)) {
            // Verifica se a coordenada contém algum navio
            if (tabuleiro[linha - 1][coluna - 1] != null) { // se tiver lá um barco retorna false
                return false
            }
        } else {
            // Coordenada fora dos limites, considera como ocupada
            return false
        }
    }
    return true
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, dimensao: Int): Boolean {
    val gerCord = gerarCoordenadasNavio(tabuleiro, linha, coluna, "E", dimensao) // coordenada do barco mancha 1
    val gerFrot = gerarCoordenadasFronteira(tabuleiro, linha, coluna, "E", dimensao)// coordenadas há volta do barco mancha 2
    val junta = juntarCoordenadas(gerCord, gerFrot)// mancha 1 e manhca 2 juntas mancha 3
    if (estaLivre(tabuleiro, junta) && gerCord.size == dimensao) { // ve se a mncha 3 está livre se estiver entra no if para colocar o barco
        for (pair in gerCord) {
            if (coordenadaContida(tabuleiro, pair.first, pair.second)) {
                tabuleiro[pair.first - 1][pair.second - 1] = dimensao.toString()[0]
            }
        }
        return true
    }
    return false
}
// unica diferença quem mete a orienteção é o utilizador
fun insereNavio(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int, orientacao: String, dimensao: Int): Boolean {
    val gerCord = gerarCoordenadasNavio(tabuleiro, linha, coluna, orientacao, dimensao)
    val gerFrot = gerarCoordenadasFronteira(tabuleiro, linha, coluna, orientacao, dimensao)
    val junta = juntarCoordenadas(gerCord, gerFrot)
    if (estaLivre(tabuleiro, junta) && gerCord.size == dimensao) {
        for (pair in gerCord) {
            if (coordenadaContida(tabuleiro, pair.first, pair.second)) {
                tabuleiro[pair.first - 1][pair.second - 1] = dimensao.toString()[0]
            }
        }
        return true
    }
    return false
}

fun preencheNavioRandom(tabuleiro: Array<Array<Char?>>, tamanho: Int): Boolean {
    val linha = (1..tabuleiro.size).random()
    val coluna = (1..tabuleiro[0].size).random()
    val indiceAleatorio = (0..3).random() // orientação random
    val orientacao = when (indiceAleatorio) {
        0 -> "S"
        1 -> "O"
        2 -> "E"
        else -> "N"
    }
    return insereNavio(tabuleiro, linha, coluna, orientacao, tamanho)
}

fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, array1: Array<Int>): Array<Array<Char?>> {
    var submarino = array1[0] // existe 2
    var contraTorpedeiros = array1[1]//existe 2
    var navioTanque = array1[2]// 1 navio de tanque
    var portaAvioes = array1[3] // 0 porta avioes

    while (portaAvioes > 0) {
        if (preencheNavioRandom(tabuleiro, 4)) {
            portaAvioes--
        }
    }

    while (navioTanque > 0) {
        if (preencheNavioRandom(tabuleiro, 3)) {
            navioTanque--
        }
    }

    while (contraTorpedeiros > 0) {
        if (preencheNavioRandom(tabuleiro, 2)) {
            contraTorpedeiros--
        }
    }


    while (submarino > 0) {
        if (preencheNavioRandom(tabuleiro, 1)) {
            submarino--
        }
    }

    return tabuleiro
}


fun navioCompleto(tabuleiro: Array<Array<Char?>>, linha: Int, coluna: Int): Boolean {
    val novaLinha = linha - 1 // ir para o tabuleiro de array
    val novaColuna = coluna - 1 // ir para o tabuleiro de array
    if (!coordenadaContida(tabuleiro, linha, coluna)) {// ver se a coordenada esta dentro do tabuleiro
        return false
    }
    when (tabuleiro[novaLinha][novaColuna]) {
        '₁', '1' -> return true
        '₂', '2' -> {
            if (coordenadaContida(tabuleiro, linha + 1, coluna)) {
                if (tabuleiro[novaLinha + 1][novaColuna] == '2' || tabuleiro[novaLinha + 1][novaColuna] == '₂') {
                    return true
                }
            }
            if (coordenadaContida(tabuleiro, linha - 1, coluna)) {// linha cima
                if (tabuleiro[novaLinha - 1][novaColuna] == '2' || tabuleiro[novaLinha - 1][novaColuna] == '₂') {
                    return true
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna + 1)) {// direita
                if (tabuleiro[novaLinha][novaColuna + 1] == '2' || tabuleiro[novaLinha][novaColuna + 1] == '₂') {
                    return true
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna - 1)) {// esquerda
                if (tabuleiro[novaLinha][novaColuna - 1] == '2' || tabuleiro[novaLinha][novaColuna - 1] == '₂') {
                    return true
                }
            }
            return false
        }

        '₃', '3' -> {
            var baixo = 0
            var cima = 0
            var esquerda = 0
            var direita = 0
            if (coordenadaContida(tabuleiro, linha + 1, coluna)) {
                if (tabuleiro[novaLinha + 1][novaColuna] == '3' || tabuleiro[novaLinha + 1][novaColuna] == '₃') {
                    baixo++
                }
            }
            if (coordenadaContida(tabuleiro, linha + 2, coluna)) {
                if (tabuleiro[novaLinha + 2][novaColuna] == '3' || tabuleiro[novaLinha + 2][novaColuna] == '₃') {
                    baixo++
                }
            }
            if (coordenadaContida(tabuleiro, linha - 1, coluna)) {
                if (tabuleiro[novaLinha - 1][novaColuna] == '3' || tabuleiro[novaLinha - 1][novaColuna] == '₃') {
                    cima++
                }
            }
            if (coordenadaContida(tabuleiro, linha - 2, coluna)) {
                if (tabuleiro[novaLinha - 2][novaColuna] == '3' || tabuleiro[novaLinha - 2][novaColuna] == '₃') {
                    cima++
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna - 1)) {
                if (tabuleiro[novaLinha][novaColuna - 1] == '3' || tabuleiro[novaLinha][novaColuna - 1] == '₃') {
                    esquerda++
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna - 2)) {
                if (tabuleiro[novaLinha][novaColuna - 2] == '3' || tabuleiro[novaLinha][novaColuna - 2] == '₃') {
                    esquerda++
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna + 1)) {
                if (tabuleiro[novaLinha][novaColuna + 1] == '3' || tabuleiro[novaLinha][novaColuna + 1] == '₃') {
                    direita++
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna + 2)) {
                if (tabuleiro[novaLinha][novaColuna + 2] == '3' || tabuleiro[novaLinha][novaColuna + 2] == '₃') {
                    direita++
                }
            }

            val soma = baixo + esquerda + direita + cima

            if (soma == 2) {// quando encontra 2 lados do barco pelas direções ele esta completo
                return true
            }

            return false
        }


        '₄', '4' -> {
            var baixo = 0
            var cima = 0
            var esquerda = 0
            var direita = 0
            if (coordenadaContida(tabuleiro, linha + 1, coluna)) {
                if (tabuleiro[novaLinha + 1][novaColuna] == '4' || tabuleiro[novaLinha + 1][novaColuna] == '₄') {
                    baixo++
                }
            }
            if (coordenadaContida(tabuleiro, linha + 2, coluna)) {
                if (tabuleiro[novaLinha + 2][novaColuna] == '4' || tabuleiro[novaLinha + 2][novaColuna] == '₄') {
                    baixo++
                }
            }
            if (coordenadaContida(tabuleiro, linha + 3, coluna)) {
                if (tabuleiro[novaLinha + 3][novaColuna] == '4' || tabuleiro[novaLinha + 3][novaColuna] == '₄') {
                    baixo++
                }
            }
            if (coordenadaContida(tabuleiro, linha - 1, coluna)) {
                if (tabuleiro[novaLinha - 1][novaColuna] == '4' || tabuleiro[novaLinha - 1][novaColuna] == '₄') {
                    cima++
                }
            }
            if (coordenadaContida(tabuleiro, linha - 2, coluna)) {
                if (tabuleiro[novaLinha - 2][novaColuna] == '4' || tabuleiro[novaLinha - 2][novaColuna] == '₄') {
                    cima++
                }
            }
            if (coordenadaContida(tabuleiro, linha - 3, coluna)) {
                if (tabuleiro[novaLinha - 3][novaColuna] == '4' || tabuleiro[novaLinha - 3][novaColuna] == '₄') {
                    cima++
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna - 1)) {
                if (tabuleiro[novaLinha][novaColuna - 1] == '4' || tabuleiro[novaLinha][novaColuna - 1] == '₄') {
                    esquerda++
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna - 2)) {
                if (tabuleiro[novaLinha][novaColuna - 2] == '4' || tabuleiro[novaLinha][novaColuna - 2] == '₄') {
                    esquerda++
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna - 3)) {
                if (tabuleiro[novaLinha][novaColuna - 3] == '4' || tabuleiro[novaLinha][novaColuna - 3] == '₄') {
                    esquerda++
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna + 1)) {
                if (tabuleiro[novaLinha][novaColuna + 1] == '4' || tabuleiro[novaLinha][novaColuna + 1] == '₄') {
                    direita++
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna + 2)) {
                if (tabuleiro[novaLinha][novaColuna + 2] == '4' || tabuleiro[novaLinha][novaColuna + 2] == '₄') {
                    direita++
                }
            }
            if (coordenadaContida(tabuleiro, linha, coluna + 3)) {
                if (tabuleiro[novaLinha][novaColuna + 3] == '4' || tabuleiro[novaLinha][novaColuna + 3] == '₄') {
                    direita++
                }
            }

            val soma = baixo + esquerda + direita + cima

            if (soma == 3) {
                return true
            }

            return false
        }

        else -> return false
    }
}

fun obtemMapa(tabuleiro: Array<Array<Char?>>, verifica: Boolean): Array<String> {
    var count = 0 // igual ao numero de linha da tabela vai sempre aumentando
    val tamanhoDoArray = tabuleiro.size + 1 // + uma linha cabeçalho
    val arrayDeStrings = Array(tamanhoDoArray) { "" }
    if (verifica) {
        var aux = "|"
        arrayDeStrings[0] = "| " + criaLegendaHorizontal(tabuleiro[0].size) + " |" // acrescentamos as linhas no inicio da primeira linha = | A | B | ...
        for (linha in tabuleiro) {
            for (elemento in linha) {
                if (elemento == null) {
                    aux += " ~ |"
                } else {
                    aux += " $elemento |"
                }
            }
            count++
            aux += " $count"
            arrayDeStrings[count] = aux
            aux = "|"
        }
        return arrayDeStrings
    } else {
        var aux = "|"
        arrayDeStrings[0] = "| " + criaLegendaHorizontal(tabuleiro[0].size) + " |"
        for (linha in tabuleiro) {
            var coluna = 0
            for (elemento in linha) {
                if (elemento == null) {
                    aux += " ? |"
                } else if (elemento == 'X') { // tiro no computador falhado
                    aux += " $elemento |"
                } else {
                    if (navioCompleto(tabuleiro, count + 1, coluna + 1)) {
                        when (elemento) {
                            '1' -> aux += " 1 |"
                            '2' -> aux += " 2 |"
                            '3' -> aux += " 3 |"
                            '4' -> aux += " 4 |"
                        }
                    } else {
                        when (elemento) {
                            '2' -> aux += " \u2082 |"
                            '3' -> aux += " \u2083 |"
                            '4' -> aux += " \u2084 |"
                        }
                    }
                }
                coluna++

            }
            count++
            aux += " $count"
            arrayDeStrings[count] = aux
            aux = "|"
        }
        return arrayDeStrings
    }
}

fun lancarTiro(
    tabuleiro1: Array<Array<Char?>>, // tabuleiroReal do computador o que tem as ondas o que quero atacar
    tabuleiro2: Array<Array<Char?>>, // tabuleiroDePalpites o tabuleiro onde aponto as coisa que falho ou acerto
    coordenadas: Pair<Int, Int>
): String {

    when (tabuleiro1[coordenadas.first - 1][coordenadas.second - 1]) {
        null -> {
            tabuleiro2[coordenadas.first - 1][coordenadas.second - 1] = 'X'
            return "Agua."
        }

        '1' -> {
            tabuleiro2[coordenadas.first - 1][coordenadas.second - 1] = '1'
            return "Tiro num submarino."
        }

        '2' -> {
            tabuleiro2[coordenadas.first - 1][coordenadas.second - 1] = '2'
            return "Tiro num contra-torpedeiro."
        }

        '3' -> {

            tabuleiro2[coordenadas.first - 1][coordenadas.second - 1] = '3'
            return "Tiro num navio-tanque."
        }

        '4' -> {
            tabuleiro2[coordenadas.first - 1][coordenadas.second - 1] = '4'
            return "Tiro num porta-avioes."
        }
    }
    return ""
}

fun geraTiroComputador(tabuleiro: Array<Array<Char?>>): Pair<Int, Int> {
    var linha = (1..tabuleiro.size).random()
    var coluna = (1..tabuleiro[0].size).random()
    while (tabuleiro[linha - 1][coluna - 1] != null) { // Enquanto diferente de null ou seja acertou num barco vai ficar a gerar novas coordenadas random até que seja agua == null acerta em todos os barco tirar o ciclo fica full random pode falhar e acertar
        linha = (1..tabuleiro.size).random()
        coluna = (1..tabuleiro[0].size).random()
    }
    /*
    while (tabuleiro[linha - 1][coluna - 1] == null) { Acerta sempre nos barcos para nao acertar tiramos os whiles para ser full random
        linha = (1..tabuleiro.size).random()
        coluna = (1..tabuleiro[0].size).random()
    }

     */
    return Pair(linha, coluna)

}
// tabuleiro numa posição qualquer se for diferente de null tem lá um barco se for igual a null é agua
fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int { // so conta os navios completos
    val unicodeChar = when (dimensao) {
        1 -> '1'
        2 -> '₂'
        3 -> '₃'
        4 -> '₄'
        else -> ' '
    }

    var count = 0
    for (linha in 0 until tabuleiro.size) {
        for (coluna in 0 until tabuleiro[linha].size) {
            if (tabuleiro[linha][coluna] != null) {
                if (tabuleiro[linha][coluna] == unicodeChar || tabuleiro[linha][coluna] == dimensao.toString()[0]) {
                    if (navioCompleto(tabuleiro, linha + 1, coluna + 1)) {
                        if (dimensao.toString()[0] == '3' || dimensao.toString()[0] == '4') {
                            return 1
                        }
                        count++
                    }
                }
            }
        }
    }
    if (dimensao.toString()[0] == '2') {
        count /= 2
        return count
    }
    return count
}

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean {
    val navios = calculaNumNavios(tabuleiro.size, tabuleiro[0].size)
    val numSubmarinos = contarNaviosDeDimensao(tabuleiro, 1)
    val numTorpedos = contarNaviosDeDimensao(tabuleiro, 2)
    val numTanques = contarNaviosDeDimensao(tabuleiro, 3)
    val numPortaAvioes = contarNaviosDeDimensao(tabuleiro, 4)
    var countNaviosArray = 0
    for (posisao in navios) {
        countNaviosArray += posisao
    }
    val countNaviosTabuleiro = numPortaAvioes + numTanques + numTorpedos + numSubmarinos
    if (countNaviosArray == countNaviosTabuleiro) {
        return true
    } else {
        return false
    }
}

fun lerJogo(nomeFicheiro: String, tipoTabuleiro: Int): Array<Array<Char?>> {

    return emptyArray()
}

fun gravarJogo(
    nomeFicheiro: String,
    tabuleiro1: Array<Array<Char?>>,
    tabuleiro2: Array<Array<Char?>>,
    tabuleiro3: Array<Array<Char?>>,
    tabuleiro4: Array<Array<Char?>>
) {
    val ficheiro = BufferedWriter(FileWriter(nomeFicheiro))
    ficheiro.write("${tabuleiro1.size},${tabuleiro1.size}\n\n")
    ficheiro.write("Jogador\nReal\n")
    for (linha in tabuleiro1) {
        var count = 0
        var linhaFicheiro = ""
        for (colunaElemento in linha) {
            if (colunaElemento==null){
                linhaFicheiro += ""
                count ++
            }else{
                linhaFicheiro += colunaElemento
                count ++
            }
            if(count<tabuleiro1[0].size){
                linhaFicheiro += ","
            }
        }
        ficheiro.write(linhaFicheiro + "\n")
    }

    ficheiro.write("\nJogador\nPalpites\n")
    for (linha in tabuleiro2) {
        var count = 0
        var linhaFicheiro = ""
        for (colunaElemento in linha) {
            if (colunaElemento==null){
                linhaFicheiro += ""
                count ++
            }else{
                linhaFicheiro += colunaElemento
                count ++
            }
            if(count<tabuleiro2[0].size){
                linhaFicheiro += ","
            }
        }
        ficheiro.write(linhaFicheiro + "\n")
    }

    ficheiro.write("\nComputador\nReal\n")
    for (linha in tabuleiro3) {
        var count = 0
        var linhaFicheiro = ""
        for (colunaElemento in linha) {
            if (colunaElemento==null){
                linhaFicheiro += ""
                count ++
            }else{
                linhaFicheiro += colunaElemento
                count++
            }
            if(count<tabuleiro3[0].size){
                linhaFicheiro += ","
            }
        }
        ficheiro.write(linhaFicheiro + "\n")
    }

    ficheiro.write("\nComputador\nPalpites\n")
    for (linha in tabuleiro4) {
        var count = 0
        var linhaFicheiro = ""
        for (colunaElemento in linha) {
            if (colunaElemento==null){
                linhaFicheiro += ""
                count ++
            }else{
                linhaFicheiro += colunaElemento
                count ++
            }
            if(count<tabuleiro4[0].size){
                linhaFicheiro += ","
            }
        }
        ficheiro.write(linhaFicheiro + "\n")
    }
    ficheiro.close()
}


fun criaTerreno(numLinhas: Int, numColunas: Int): String {
    var tamanhopipes = "| ~ "
    var pipes = ""
    var linhas = 1
    var primeiraLinha = ""

    primeiraLinha += "| " + criaLegendaHorizontal(numColunas) + " |\n"

    var terrenoTotal = ""
    terrenoTotal += primeiraLinha

    while (linhas <= numLinhas) {
        var colunas = 0
        while (tamanhopipes.length <= primeiraLinha.length) {
            if (colunas < numColunas - 1) {
                tamanhopipes += "| ~ "
            } else {
                tamanhopipes += "| $linhas\n"
            }
            colunas++
        }
        linhas++
        pipes += tamanhopipes
        tamanhopipes = "| ~ "
    }

    terrenoTotal += pipes
    return terrenoTotal
}

fun menuPrincipal(): Int {

    println("\n> > Batalha Naval < <\n")
    println("1 - Definir Tabuleiro e Navios")
    println("2 - Jogar")
    println("3 - Gravar")
    println("4 - Ler")
    println("0 - Sair\n")

    var opcao = readLine()?.toIntOrNull()

    while (opcao !in (0..4)) {
        println("!!! Opcao invalida, tente novamente")
        opcao = readLine()?.toIntOrNull()
    }

    when (opcao) {
        1 -> return 101
        2 -> return if ((tabuleiroHumano.isEmpty() || tabuleiroHumano.all { it.isEmpty() })) { // so funciona se o tabuleiro tiver sido defenido
            println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
            100
        } else {
            103
        }
        3 -> return if ((tabuleiroHumano.isEmpty() || tabuleiroHumano.all { it.isEmpty() })) { // so funciona se os tabuleiros tiverem defenidos
            println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
            100
        } else {
            105
        }
        4 -> return 104
        0 -> return 106
        else -> return 100
    }
}


fun menuDefinirTabuleiro(): Int {
    println("\n> > Batalha Naval < <\n")
    println("Defina o tamanho do tabuleiro:")
    println("Quantas linhas?")
    var linhas = readln().toIntOrNull()
    if (linhas == -1) { // return para o menu
        return 100
    }
    while (linhas == null) {
        println("!!! Numero de linhas invalidas, tente novamente")
        println("Quantas linhas?")
        linhas = readln().toIntOrNull()
    }
    println("Quantas colunas?")

    var colunas = readln().toIntOrNull()
    if (colunas == -1) {
        return 100
    }

    while (colunas == null) {
        println("!!! Numero de colunas invalidas, tente novamente")
        println("Quantas colunas?")
        colunas = readln().toIntOrNull()
    }
    if (!tamanhoTabuleiroValido(linhas, colunas)) {
        println("!!! Tamanho invalido")

    }
    print(criaTerreno(linhas, colunas))
    numLinhas = linhas
    numColunas = colunas

    return 102
}


fun menuDefinirNavios(): Int {
    calculaNumNavios(numLinhas, numColunas) // n faz nada
    val navios = calculaNumNavios(numLinhas, numColunas)
    var submarino = navios[0]
    var contraTorpedeiros = navios[1]
    var navioTanque = navios[2]
    var portaAvioes = navios[3]
    val exemplocoordenadas = "Coordenadas? (ex: 6,G)"
    val orientacaoerro = "!!! Orientacao invalida, tente novamente"
    val coordenadaserro = "!!! Coordenadas invalidas, tente novamente"
    val orientacaoPrint = "Orientacao? (N, S, E, O)"
    val orientacaonavio = "Insira a orientacao do navio:"
    tabuleiroHumano = criaTabuleiroVazio(numLinhas, numColunas)
    tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)
    tabuleiroPalpitesDoComputador = criaTabuleiroVazio(numLinhas, numColunas)
    tabuleiroPalpitesDoHumano = criaTabuleiroVazio(numLinhas, numColunas)
    while (submarino > 0) {
        do {

            println("Insira as coordenadas de um submarino:")
            println(exemplocoordenadas)
            var coordenadas = readln()
            val coordeint = coordenadas.toIntOrNull()
            if (coordeint == -1) {
                return 100
            }

            while (processaCoordenadas(coordenadas, numLinhas, numColunas) == null) {
                println(coordenadaserro)
                println(exemplocoordenadas)
                coordenadas = readln()

            }
            val coordecertas = processaCoordenadas(coordenadas, numLinhas, numColunas)
            val linha = coordecertas?.first ?: -1
            val coluna = coordecertas?.second ?: -1
        } while (!insereNavioSimples(tabuleiroHumano, linha, coluna, 1))

        submarino--
        val mapa = obtemMapa(tabuleiroHumano, true) // dar println no mapa
        for (string in mapa) {
            println(string)
        }
    }

    while (contraTorpedeiros > 0) {
        do {
            println("Insira as coordenadas de um contra-torpedeiro:")
            println(exemplocoordenadas)
            var coordenadas = readln()
            val coordeint = coordenadas.toIntOrNull()
            if (coordeint == -1) {
                return 100
            }

            while (processaCoordenadas(coordenadas, numLinhas, numColunas) == null) {
                println(coordenadaserro)
                println(exemplocoordenadas)
                coordenadas = readln()
            }

            val coordecertas = processaCoordenadas(coordenadas, numLinhas, numColunas)
            val linha = coordecertas?.first ?: -1 // pode ser null por ieeo ?. + elvis
            val coluna = coordecertas?.second ?: -1

            println(orientacaonavio)
            println(orientacaoPrint)
            var orientacao = readln()
            val orientint = orientacao.toIntOrNull()
            if (orientint == -1) {
                return 100
            }

            while (orientacao != "N" && orientacao != "S" && orientacao != "E" && orientacao != "O" || orientacao == null) {
                println(orientacaoerro)
                println(orientacaoPrint)
                orientacao = readln()
            }

            // Adicione a chave de fechamento aqui
        } while (!insereNavio(tabuleiroHumano, linha, coluna, orientacao, 2))

        contraTorpedeiros--
        val mapa = obtemMapa(tabuleiroHumano, true)
        for (string in mapa) {
            println(string)
        }
    }

    while (navioTanque > 0) {
        do {
            println("Insira as coordenadas de um navio-tanque:")
            println(exemplocoordenadas)
            var coordenadas = readln()
            val coordeint = coordenadas.toIntOrNull()
            if (coordeint == -1) {
                return 100
            }

            while (processaCoordenadas(coordenadas, numLinhas, numColunas) == null) {
                println(coordenadaserro)
                println(exemplocoordenadas)
                coordenadas = readln()
            }

            val coordecertas = processaCoordenadas(coordenadas, numLinhas, numColunas)
            val linha = coordecertas?.first ?: -1
            val coluna = coordecertas?.second ?: -1

            println(orientacaonavio)
            println(orientacaoPrint)
            var orientacao = readln()
            val orientint = orientacao.toIntOrNull()
            if (orientint == -1) {
                return 100
            }

            while (orientacao != "N" && orientacao != "S" && orientacao != "E" && orientacao != "O" || orientacao == null) {
                println(orientacaoerro)
                println(orientacaoPrint)
                orientacao = readln()
            }

            // Adicione a chave de fechamento aqui
        } while (!insereNavio(tabuleiroHumano, linha, coluna, orientacao, 3))

        navioTanque--
        val mapa = obtemMapa(tabuleiroHumano, true)
        for (string in mapa) {
            println(string)
        }
    }

    while (portaAvioes > 0) {
        do {
            println("Insira as coordenadas de um porta-avioes:")
            println(exemplocoordenadas)
            var coordenadas = readln()
            val coordeint = coordenadas.toIntOrNull()
            if (coordeint == -1) {
                return 100
            }

            while (processaCoordenadas(coordenadas, numLinhas, numColunas) == null) {
                println(coordenadaserro)
                println(exemplocoordenadas)
                coordenadas = readln()
            }

            val coordecertas = processaCoordenadas(coordenadas, numLinhas, numColunas)
            val linha = coordecertas?.first ?: -1
            val coluna = coordecertas?.second ?: -1

            println(orientacaonavio)
            println(orientacaoPrint)
            var orientacao = readln()
            val orientint = orientacao.toIntOrNull()
            if (orientint == -1) {
                return 100
            }

            while (orientacao != "N" && orientacao != "S" && orientacao != "E" && orientacao != "O" || orientacao == null) {
                println(orientacaoerro)
                println(orientacaoPrint)
                orientacao = readln()
            }

            // Adicione a chave de fechamento aqui
        } while (!insereNavio(tabuleiroHumano, linha, coluna, orientacao, 4))

        portaAvioes--
        val mapa = obtemMapa(tabuleiroHumano, true)
        for (string in mapa) {
            println(string)
        }
    }
    preencheTabuleiroComputador(tabuleiroComputador, navios)
    println("Pretende ver o mapa gerado para o Computador? (S/N)")

    val resposta = readln()
    if (resposta == "S") {
        val mapa = obtemMapa(tabuleiroComputador, true)
        for (string in mapa) {
            println(string)
        }
        return 100
    }
    return 100
}


fun menuJogar(): Int {

    while (!venceu(tabuleiroPalpitesDoHumano) && !venceu(tabuleiroPalpitesDoComputador)) {

        val mapa = obtemMapa(tabuleiroPalpitesDoHumano, false)
        for (string in mapa) {
            println(string)
        }
        /*
        var traco = "-"
        while(traco.length < criaLegendaHorizontal(numColunas).length + 6 ){
            traco += "-"
        }
        println(traco)

         */

        println("Indique a posição que pretende atingir")
        println("Coordenadas? (ex: 6,G)")

        var coordenadas = readln()
        val coordeint = coordenadas.toIntOrNull()
        if (coordeint == -1) {
            return 100
        }
        if(coordenadas == "?") {
            var naviosemfalta = calculaNaviosFaltaAfundar(tabuleiroPalpitesDoHumano)
            var sub = naviosemfalta[3]
            var contra = naviosemfalta[2]
            var tank = naviosemfalta[1]
            var aviao = naviosemfalta[0]
            var string = "Falta afundar: "
            var pontoevirgula = "; "
            if (aviao != 0) {
                string += "$aviao Porta-avioes(s)"
                string += pontoevirgula
            }
            if (tank != 0) {
                string += "$tank navio-tanque(s)"
                string += pontoevirgula
            }
            if (contra != 0) {
                string += "$contra contra-torpedeiro(s)"
                string += pontoevirgula
            }
            if (sub != 0) {
                string += "$sub submarino(s)"
                string += pontoevirgula
            }

            string = string.dropLast(2)
            println(string)
            println("Indique a posição que pretende atingir")
            println("Coordenadas? (ex: 6,G)")
            coordenadas = readln()
            val coordeint1 = coordenadas.toIntOrNull()
            if (coordeint1 == -1) {
                return 100
            }
        }



        while (processaCoordenadas(coordenadas, numLinhas, numColunas) == null) {
            println("!!! Coordenadas invalidas, tente novamente")
            println("Coordenadas? (ex: 6,G)")
            coordenadas = readln()
        }
        val coordecertas = processaCoordenadas(coordenadas, numLinhas, numColunas)
        if (coordecertas != null) {
            print(">>> HUMANO >>>")
            val coordenadaTiro = lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordecertas)// ataca o tabuleiro real do computador e aponta no meu tabuleiro de palpites
            if (navioCompleto(tabuleiroPalpitesDoHumano, coordecertas.first, coordecertas.second)) {
                println("$coordenadaTiro Navio ao fundo!")
            } else {
                println(coordenadaTiro)
            }
        }
        if (venceu(tabuleiroPalpitesDoHumano)) {
            println("PARABENS! Venceu o jogo!")
            println("Prima enter para voltar ao menu principal")
            do {
                val enter = readln()

            } while (enter.isNotEmpty())
            return 100
        }

        val coordenadasPc = geraTiroComputador(tabuleiroHumano)// gera tiros para a água fun random
        print("Computador lancou um tiro para a posicao ")
        println(coordenadasPc)
        print(">>> COMPUTADOR >>>")
        val coordenadaTiroPc = lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, coordenadasPc)
        if (navioCompleto(tabuleiroPalpitesDoComputador, coordenadasPc.first, coordenadasPc.second)) {
            println("$coordenadaTiroPc Navio ao fundo!")
        } else {
            println(coordenadaTiroPc)
        }
        println("Prima enter para continuar")
        do {
            val enter = readln()

        } while (enter.isNotEmpty())
    }
    return 100
}

fun menuLerFicheiro(): Int {
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeFicheiro = readlnOrNull() ?: return 100

    try {
        val leitor = BufferedReader(FileReader(nomeFicheiro))

        // Lógica de leitura do arquivo
        val tamanho = leitor.readLine().split(",")
        numLinhas = tamanho[0].toInt()
        numColunas = tamanho[1].toInt()

        // Pula as linhas em branco e os cabeçalhos
        leitor.readLine()
        leitor.readLine()
        leitor.readLine()

        // Lê o tabuleiro do jogador real
        tabuleiroHumano = criaTabuleiroVazio(numLinhas,numColunas)

        for (i in 0 until numLinhas) {
            val linhaFicheiro = leitor.readLine().split(",")
            for (j in 0 until numColunas) {
                if (linhaFicheiro[j].isNotEmpty()) {
                    tabuleiroHumano[i][j] = linhaFicheiro[j][0]
                }
            }
        }

        // Pula os cabeçalhos
        leitor.readLine()
        leitor.readLine()
        leitor.readLine()

        // Lê o tabuleiro de palpites do jogador
        tabuleiroPalpitesDoHumano = criaTabuleiroVazio(numLinhas, numColunas)

        for (i in 0 until numLinhas) {
            val linhaFicheiro = leitor.readLine().split(",")
            for (j in 0 until numColunas) {
                if (linhaFicheiro[j].isNotEmpty()) {
                    tabuleiroPalpitesDoHumano[i][j] = linhaFicheiro[j][0]
                }
            }
        }

        leitor.readLine()
        leitor.readLine()
        leitor.readLine()

        tabuleiroComputador = criaTabuleiroVazio(numLinhas, numColunas)

        for (i in 0 until numLinhas) {
            val linhaFicheiro = leitor.readLine().split(",")
            for (j in 0 until numColunas) {
                if (linhaFicheiro[j].isNotEmpty()) {
                    tabuleiroComputador[i][j] = linhaFicheiro[j][0]
                }
            }
        }

        leitor.readLine()
        leitor.readLine()
        leitor.readLine()

        // Lê o tabuleiro de palpites do computador
        tabuleiroPalpitesDoComputador = criaTabuleiroVazio(numLinhas, numColunas)

        for (i in 0 until numLinhas) {
            val linhaFicheiro = leitor.readLine().split(",")
            for (j in 0 until numColunas) {
                if (linhaFicheiro[j].isNotEmpty()) {
                    tabuleiroPalpitesDoComputador[i][j] = linhaFicheiro[j][0]
                }
            }
        }

        leitor.close()


        println("Tabuleiro $numLinhas"+"x"+"$numColunas lido com sucesso")
    } catch (ex: Exception) {
        println("Erro ao ler o arquivo. Verifique se o nome do arquivo é válido.")
        return -1
    }

    val jogoLido = obtemMapa(tabuleiroHumano,true)

    for (string in jogoLido) {
        println(string)
    }

    return 100
}
fun menuGravarFicheiro(): Int {
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeFicheiro = readln()
    gravarJogo(nomeFicheiro,tabuleiroHumano,tabuleiroPalpitesDoHumano,tabuleiroComputador,tabuleiroPalpitesDoComputador)
    println("Tabuleiro $numLinhas"+"x"+"$numColunas gravado com sucesso")
    return 100

}

var numLinhas = -1
var numColunas = -1


fun main() {

    var menuActual = MENU_PRINCIPAL
    while (true) {
        menuActual = when (menuActual) {
            MENU_PRINCIPAL -> menuPrincipal()
            MENU_DEFINIR_TABULEIRO -> menuDefinirTabuleiro()
            MENU_DEFINIR_NAVIOS -> menuDefinirNavios()
            MENU_JOGAR -> menuJogar()
            MENU_LER_FICHEIRO -> menuLerFicheiro()
            MENU_GRAVAR_FICHEIRO -> menuGravarFicheiro()
            SAIR -> return
            else -> return
        }
    }
}