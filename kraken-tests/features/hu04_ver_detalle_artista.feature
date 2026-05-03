Feature: Visualizar información detallada de un artista
  Como usuario visitante quiero ver el detalle de un artista para ampliar la información sobre él.

  @user1 @mobile
  Scenario: Ver todos los campos en la vista de detalle
    Given I wait for the view with id "card_visitor" to be displayed
    And I click on the view with id "card_visitor"
    And I wait for the view with id "musicianFragment" to be displayed
    And I click on the view with id "musicianFragment"
    And I wait for the view with id "rv_musicians" to be displayed
    And I click on the view with id "tv_musician_name"
    Then I wait for the view with id "ivPhoto" to be displayed
    And I wait for the view with id "tvName" to be displayed
    And I wait for the view with id "tvDescription" to be displayed
    And I wait for the view with id "tvBirthDate" to be displayed
    And I wait for the view with id "rvAlbums" to be displayed
