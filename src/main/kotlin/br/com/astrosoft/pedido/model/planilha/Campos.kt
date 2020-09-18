package br.com.astrosoft.pedido.model.planilha

import java.time.LocalDate

open class Campo<T: Any, B>(val header: String, val produceVakue: (B) -> T)

class CampoString<B>(header: String, produceVakue: (B) -> String = {""}): Campo<String, B>(header, produceVakue)
class CampoNumber<B>(header: String, produceVakue: (B) -> Double = {0.00}): Campo<Double, B>(header, produceVakue)
class CampoInt<B>(header: String, produceVakue: (B) -> Int = {0}): Campo<Int, B>(header, produceVakue)
