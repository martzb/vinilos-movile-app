Feature: Consultar catálogo de álbumes
  Como usuario visitante quiero navegar el catálogo de álbumes para escoger los que más me interesan.

  @user1 @mobile
  Scenario: Visualizar correctamente la lista de álbumes
    Given I wait for the view with id "card_visitor" to be displayed
    When I click on the view with id "card_visitor"
    Then I wait for the view with id "rv_trending" to be displayed
    And I wait for the view with id "tv_album_name" to be displayed
    And I wait for the view with id "iv_cover" to be displayed
