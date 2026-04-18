Feature: Regresar al catálogo desde el detalle
  Como usuario visitante quiero ver el detalle de un álbum para ampliar la información sobre él.

  @user1 @mobile
  Scenario: Usar flecha de atrás para volver al listado
    Given I wait for the view with id "card_visitor" to be displayed
    And I click on the view with id "card_visitor"
    And I wait for the view with id "rv_trending" to be displayed
    And I click on the view with id "tv_album_name"
    And I wait for the view with id "albumTitle" to be displayed
    When I click on the view with id "btnBack"
    Then I wait for the view with id "rv_trending" to be displayed
