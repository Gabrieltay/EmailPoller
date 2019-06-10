fun main(args: Array<String>) {

    val protocol: String = "imap"
    val host: String = "imap.gmail.com"
    val port: String = "993"

    val userName: String = "gds.ace.gpls@gmail.com"
    val password: String = "ilovethanos"

    val emailController  = EmailController(protocol, host, port, userName, password)
    emailController.downloadEmails()
}