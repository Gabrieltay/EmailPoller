import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*
import javax.mail.*
import javax.mail.internet.MimeMultipart
import javax.mail.search.FlagTerm

class EmailController(val protocol: String,
                      val host: String,
                      val port: String,
                      val userName: String,
                      val password: String) {
    lateinit var properties: Properties

    init {
        properties = Properties()
        // Server setting
        properties.put("mail.$protocol.host", host)
        properties.put("mail.$protocol.port", port)

        // SSL setting
        properties.setProperty("mail.$protocol.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        properties.setProperty("mail.$protocol.socketFactory.fallback", "false")
        properties.setProperty("mail.$protocol.socketFactory.port", port)
    }

    fun downloadEmails(){
        val session: Session = Session.getDefaultInstance(properties)
        // connects to the message store
        val store: Store = session.getStore(protocol)
        store.connect(userName, password)

        // opens inbox folder
        val folderInbox: Folder = store.getFolder("INBOX")
        folderInbox.open(Folder.READ_WRITE)

        // set unseen filter
        val unseenFlagTerm: FlagTerm = FlagTerm(Flags(Flags.Flag.SEEN), false)

        // fetches new messages from server
        folderInbox.search(unseenFlagTerm).forEach {
            println("From: ${it.from.map { it }.joinToString()}")
            println("Subject: ${it.subject}")
            println("Message: ${getMessageContent(it)}")

            // Update message as SEEN
            it.setFlag(Flags.Flag.SEEN, true)
        }

        // disconnect
        folderInbox.close(false)
        store.close()
    }

    fun getMessageContent(message:Message): String {
        var content: String? = null

        if ( message.content is MimeMultipart ) {
            val emailContent: StringBuilder = StringBuilder()
            parseMultipart(message.content as MimeMultipart, emailContent)

            content = emailContent.toString()
        } else {
            content = message.content.toString()
        }

        return content.replace("[^\\x00-\\x7F]", "");
    }

    fun parseMultipart(mimeMultipart: MimeMultipart, emailContent: StringBuilder) {
        val sequence = (0..mimeMultipart.count-1).asSequence()

        for ( index in sequence ) {
            val bodyPart:BodyPart = mimeMultipart.getBodyPart(index)
            if ( bodyPart.content is MimeMultipart ) {
                parseMultipart(bodyPart.content as MimeMultipart, emailContent)
            }

            if ( bodyPart.contentType.toLowerCase().contains("text/plain")) {
                // Email Content Message
                emailContent.append(bodyPart.content)
            }
            else if ( Part.ATTACHMENT.equals(bodyPart.disposition, true) ) {
                // Email Content Attachment
                val inputStream: InputStream = bodyPart.inputStream
                val fileOutput: FileOutputStream = FileOutputStream(File(bodyPart.fileName))
                inputStream.copyTo(fileOutput)
                fileOutput.close()
            }
        }
    }
}