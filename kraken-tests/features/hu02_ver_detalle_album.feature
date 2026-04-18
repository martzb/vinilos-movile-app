Feature: Visualizar información detallada de un álbum
  Como usuario visitante quiero ver el detalle de un álbum para ampliar la información sobre él.

  @user1 @mobile
  Scenario: Ver todos los campos en la vista de detalle
    Given I wait for the view with id "card_visitor" to be displayed
    And I click on the view with id "card_visitor"
    And I wait for the view with id "rv_trending" to be displayed
    And I click on the view with id "tv_album_name"
    Then I wait for the view with id "albumCover" to be displayed
    And I wait for the view with id "albumTitle" to be displayed
    And I wait for the view with id "albumArtist" to be displayed
    And I wait for the view with id "albumYear" to be displayed
    And I wait for the view with id "albumDescription" to be displayed
