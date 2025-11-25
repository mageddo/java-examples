import gg.acai.chatgpt.ChatGPT;

public class Main {
  public static void main(String[] args) {
    final var chatGpt = ChatGPT
        .newBuilder()
        .sessionToken("") // required field: get from cookies
        .cfClearance("") // required to bypass Cloudflare: get from cookies
        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/141.0.0.0 Safari/537.36") // required to bypass Cloudflare: google 'what is my user
        // agent'
//        .addExceptionAttribute(new ParsedExceptionEntry("exception keyword", Exception.class)) //
        // optional: adds an exception attribute
        .connectTimeout(60L) // optional: specify custom connection timeout limit
        .readTimeout(30L) // optional: specify custom read timeout limit
        .writeTimeout(30L) // optional: specify custom write timeout limit
        .build(); // builds the ChatGPT client

    final var conversation = chatGpt.createConversation();
    final var res = conversation.sendMessage("""
        Me responda em formato text plain exatamente o que eu te perguntar,
        que dia é hoje em formato iso 8601
        """
    );
    System.out.println(res.getMessage());
  }
}
