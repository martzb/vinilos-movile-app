Feature: Scroll en catálogo de álbumes
  Como usuario visitante quiero navegar el catálogo de álbumes para escoger los que más me interesan.

  @user1 @mobile
  Scenario: Hacer scroll en la lista para ver más álbumes
    Given I wait for the view with id "card_visitor" to be displayed
    When I click on the view with id "card_visitor"
    And I wait for the view with id "rv_trending" to be displayed
    Then I scroll down
