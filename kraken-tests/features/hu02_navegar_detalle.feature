Feature: Navegar al detalle de un álbum
  Como usuario visitante quiero ver el detalle de un álbum para ampliar la información sobre él.

  @user1 @mobile
  Scenario: Tocar una card y navegar al detalle
    Given I wait for the view with id "card_visitor" to be displayed
    And I click on the view with id "card_visitor"
    And I wait for the view with id "rv_trending" to be displayed
    When I click on the view with id "tv_album_name"
    Then I wait for the view with id "albumTitle" to be displayed
