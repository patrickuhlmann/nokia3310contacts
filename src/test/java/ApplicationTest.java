import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class ApplicationTest {
  @Test
  public void convertTest() throws IOException, URISyntaxException {
    Path input = Paths.get(Objects.requireNonNull(getClass().getResource("contacts.vcf")).toURI());
    Path resourceFolder = input.getParent();
    assertNotNull(resourceFolder);
    Path output = Paths.get(resourceFolder.toString(), "contacts_out.vcf");
    Files.deleteIfExists(output);
    Application.main(new String[] { input.toString(), output.toString()});
    assertTrue(Files.exists(output), "output file must exist");
    assertEquals(467, Files.size(output));
  }
}
