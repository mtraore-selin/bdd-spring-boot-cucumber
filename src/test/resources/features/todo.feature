Feature: Todo Management
  As an API client
  I want to manage my todos
  So that I can track and organize tasks effectively

  Background:
    Given there are 0 todos in the system

  Scenario: Create a new todo
    Given I have a todo with title "Buy groceries" and completed "false"
    When I create the todo
    Then the todo should be saved successfully

  Scenario: Get all todos
    Given there are 3 todos in the system
    When I request all todos
    Then I should receive 3 todos

  Scenario: Get a todo by ID
    Given I have a saved todo with title "Read a book" and completed "true"
    When I request the todo by ID
    Then the response should contain the todo with title "Read a book"

  Scenario: Delete a todo
    Given I have a saved todo with title "Wash car" and completed "false"
    When I delete the todo
    Then the todo should be removed from the system
