package test.echo.def

import org.kotlin.everywhere.net.Kenet

class Def : Kenet() {
    val echo by c<String, String>()
}