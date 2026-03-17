package com.efpcode.arch;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.efpcode")
public class LayerRulesTest {

  @ArchTest
  static final ArchRule layersShouldRespectHexagonalBoundaries =
      layeredArchitecture()
          .consideringAllDependencies()
          .layer("Domain")
          .definedBy("com.efpcode.domain..")
          .layer("Application")
          .definedBy("com.efpcode.application..")
          .layer("Infrastructure")
          .definedBy("com.efpcode.infrastructure..")
          .whereLayer("Domain")
          .mayOnlyBeAccessedByLayers("Application", "Infrastructure")
          .whereLayer("Application")
          .mayOnlyBeAccessedByLayers("Infrastructure")
          .whereLayer("Infrastructure")
          .mayNotBeAccessedByAnyLayer();

  @ArchTest
  static final ArchRule domainShouldBeFrameworkFree =
      noClasses()
          .that()
          .resideInAPackage("com.efpcode.domain..")
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage("org.springframework..", "jakarta.persistence..")
          .because("The domain layer must be plain old Java to avoid infrastructure leakage.");
}
