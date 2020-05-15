package org.thymeleaf.templateresource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

import org.thymeleaf.util.ClassLoaderUtils;
import org.thymeleaf.util.StringUtils;
import org.thymeleaf.util.Validate;

@TargetClass(org.thymeleaf.templateresource.ClassLoaderTemplateResource.class)
final class Target_org_thymeleaf_templateresource_ClassLoaderTemplateResource {

  @Alias
  private ClassLoader optionalClassLoader;

  @Alias
  private String path;

  @Alias
  private String characterEncoding;

  /**
   * Constructor.
   *
   * Original version removes '/' character from the start of the path, it causes the resource to not be found
   * while running on native-image binary
   */
  @Substitute
  public Target_org_thymeleaf_templateresource_ClassLoaderTemplateResource(
      final ClassLoader classLoader,
      final String path,
      final String characterEncoding
  ) {
    super();

    // Class Loader CAN be null (will apply the default sequence of class loaders
    Validate.notEmpty(path, "Resource Path cannot be null or empty");
    // Character encoding CAN be null (system default will be used)

    this.optionalClassLoader = classLoader;
    this.path = TemplateResourceUtils.cleanPath(path);
    this.characterEncoding = characterEncoding;
  }

  @Substitute
  public Reader reader() throws IOException {

      final InputStream inputStream;
      if (this.optionalClassLoader != null) {
        inputStream = this.optionalClassLoader.getResourceAsStream(this.path);
      } else {
        inputStream = ClassLoaderUtils.findResourceAsStream(this.path);
      }

      if (inputStream == null) {
        throw new FileNotFoundException(String.format("ClassLoader resource \"%s\" could not be resolved", this
       .path));
      }

      if (!StringUtils.isEmptyOrWhitespace(this.characterEncoding)) {
        return new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream), this
       .characterEncoding));
      }

      return new BufferedReader(new InputStreamReader(new BufferedInputStream(inputStream)));

  }
}
