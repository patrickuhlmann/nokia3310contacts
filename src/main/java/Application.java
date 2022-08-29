import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.ValidationWarnings;
import ezvcard.parameter.EmailType;
import ezvcard.property.Categories;
import ezvcard.property.Email;
import ezvcard.property.Nickname;
import ezvcard.property.ProductId;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

/** Converts an vcf from nextcloud (v4) to vcf for Nokia 3310 (v2.1) */
public class Application {
  private static final String ANSI_RESET = "\u001B[0m";
  private static final String ANSI_RED = "\u001B[31m";
  private static final String ANSI_GREEN = "\u001B[32m";
  private static final String ANSI_YELLOW = "\u001B[33m";
  private static final int NUMBER_OF_ARGUMENTS = 2;
  private static final int NUMBER_OF_SUPPORTED_EMAIL_ADDR = 1;

  /**
   * Entry point of the application.
   *
   * @param args first element is the inputPath, second the outputPath
   */
  public static void main(String[] args) {
    if (args.length != NUMBER_OF_ARGUMENTS) {
      System.out.println("Usage <inputPath> <outputPath>");
      return;
    }
    File input = Paths.get(args[0]).toFile();
    if (!input.exists()) {
      System.out.println(ANSI_RED + "Input file doesn't exist!" + ANSI_RESET);
      return;
    }
    File output = Paths.get(args[1]).toFile();

    new Application(input, output);
  }

  /**
   * Transition from the Static Part to the Application.
   *
   * @param input vcf from nextcloud
   * @param output vcf for nokia 3310
   */
  public Application(File input, File output) {
    List<VCard> vcards;
    try {
      vcards = Ezvcard.parse(input).all();
    } catch (IOException e) {
      System.out.println(ANSI_RED + "Failed to read input file: " + e + ANSI_RESET);
      return;
    }

    System.out.println("input vCards size = " + vcards.size());
    System.out.println(
        "the conversion will remove the following properties: PRODID, CATEGORIES, NICKNAME");
    System.out.println(
        "Nokia 3310 only supports one email thus only the first one will be kept"
        + "and all others removed");
    System.out.println(
        "it will convert emails of all types to type INTERNET for compatibility reasons");
    System.out.println("if there are additional validation problems, they will be printed");

    Iterator<VCard> it = vcards.iterator();
    while (it.hasNext()) {
      VCard vcard = it.next();
      vcard.setVersion(VCardVersion.V2_1);
      vcard.setProductId((ProductId) null);
      vcard.setCategories((Categories) null);
      vcard.setNickname((Nickname) null);
      if (vcard.getEmails().size() > NUMBER_OF_SUPPORTED_EMAIL_ADDR) {
        System.out.println(
            ANSI_YELLOW + "Only one email is supported by nokia. Only keep the first" + ANSI_RESET);
        for (Email mail : vcard.getEmails()) {
          System.out.println(ANSI_YELLOW + mail + ANSI_RESET);
        }
        System.out.println("\n");
        Email first = vcard.getEmails().get(0);
        vcard.getEmails().clear();
        vcard.getEmails().add(first);
      }

      if (vcard.getEmails().size() > 0) {
        Email email = vcard.getEmails().get(0);
        email.getTypes().clear();
        email.getTypes().add(EmailType.INTERNET);
      }

      ValidationWarnings warnings = vcard.validate(VCardVersion.V2_1);
      if (warnings.toString().trim().length() > 0) {
        System.out.println(ANSI_YELLOW + "Validaton error (entry skipped)" + ANSI_RESET);
        System.out.println(vcard);
        it.remove();
        System.out.println(ANSI_YELLOW + warnings + ANSI_RESET);
        System.out.println("\n");
      }
    }

    try {
      Ezvcard.write(vcards).go(output);
    } catch (IOException e) {
      System.out.println(ANSI_RED + "Failed to write output file: " + e + ANSI_RESET);
      return;
    }

    System.out.println(ANSI_GREEN + "Successfully wrote output file" + ANSI_RESET);
  }
}
