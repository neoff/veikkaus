/*
package fi.veikkaus.demo.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.lang.syntax.elements.GivenClasses;
import fi.veikkaus.demo.VeikkausApplication;
import java.net.URL;

import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.Configurations.consideringOnlyDependenciesInDiagram;
import static com.tngtech.archunit.library.plantuml.PlantUmlArchCondition.adhereToPlantUmlDiagram;

@AnalyzeClasses(packagesOf = {VeikkausApplication.class},
                importOptions = ImportOption.DoNotIncludeTests.class)
public class LayerDependencyRulesTest {
  private static final GivenClasses classes = ArchRuleDefinition.classes();
  private static final URL diagram = LayerDependencyRulesTest.class.getResource("/arch/layers.puml");
  @ArchTest
  public static ArchRule u;
  
  static {
    assert diagram != null;
    u = classes
            .should(adhereToPlantUmlDiagram(diagram, consideringOnlyDependenciesInDiagram()))
            .because("The dependency contradicts the layer design (layers.puml).");
  }
  
}
*/
