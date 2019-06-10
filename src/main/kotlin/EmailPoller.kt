fun main(args: Array<String>) {

    val protocol: String = "imap"
    val host: String = "imap.gmail.com"
    val port: String = "993"

    // email address and password
    val userName: String = ""
    val password: String = ""

    val emailController  = EmailController(protocol, host, port, userName, password)
    emailController.downloadEmails()
}