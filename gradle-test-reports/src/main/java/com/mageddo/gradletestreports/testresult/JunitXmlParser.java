package com.mageddo.gradletestreports.testresult;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import jakarta.inject.Singleton;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Singleton
public class JunitXmlParser {

  public List<TestCase> parseCases(final Path xmlFile) {
    final var document = this.parseDocument(xmlFile);
    final var nodes = document.getElementsByTagName("testcase");
    final var cases = new ArrayList<TestCase>(nodes.getLength());
    for (var i = 0; i < nodes.getLength(); i++) {
      cases.add(this.toTestCase((Element) nodes.item(i)));
    }
    return cases;
  }

  private TestCase toTestCase(final Element element) {
    final var classname = element.getAttribute("classname");
    return TestCase.builder()
        .className(this.simpleName(classname))
        .packageName(classname)
        .name(element.getAttribute("name"))
        .durationSeconds(this.parseSeconds(element.getAttribute("time")))
        .outcome(this.resolveOutcome(element))
        .build();
  }

  private TestOutcome resolveOutcome(final Element element) {
    if (this.hasChild(element, "failure") || this.hasChild(element, "error")) {
      return TestOutcome.FAILED;
    }
    if (this.hasChild(element, "skipped")) {
      return TestOutcome.SKIPPED;
    }
    return TestOutcome.PASSED;
  }

  private boolean hasChild(final Element element, final String tag) {
    final NodeList children = element.getChildNodes();
    for (var i = 0; i < children.getLength(); i++) {
      final Node child = children.item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE && tag.equals(child.getNodeName())) {
        return true;
      }
    }
    return false;
  }

  private String simpleName(final String classname) {
    final var idx = classname.lastIndexOf('.');
    return idx >= 0 ? classname.substring(idx + 1) : classname;
  }

  private double parseSeconds(final String time) {
    if (time == null || time.isBlank()) {
      return 0d;
    }
    return Double.parseDouble(time.trim());
  }

  private org.w3c.dom.Document parseDocument(final Path xmlFile) {
    try (final InputStream in = Files.newInputStream(xmlFile)) {
      final var factory = DocumentBuilderFactory.newInstance();
      factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      return factory.newDocumentBuilder().parse(in);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (SAXException | javax.xml.parsers.ParserConfigurationException e) {
      throw new IllegalStateException("Failed to parse junit report: " + xmlFile, e);
    }
  }
}
