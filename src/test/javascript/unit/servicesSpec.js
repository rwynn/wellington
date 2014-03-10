'use strict';

/* jasmine specs for services go here */

describe('Register Service', function() {
  var registerService, $httpBackend, messageService;
  beforeEach(module('wellington.services'));
  beforeEach(inject(function(RegisterService, MessageService, _$httpBackend_) {
      registerService = RegisterService;
      messageService = MessageService;
      $httpBackend = _$httpBackend_;
  }));

  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  describe('register', function() {
    it('should handle a success', function() {
        $httpBackend.expectPOST('/users/register', {
            "username": "user@email.com",
            "password": "user"
        }).respond({ success: true });
        var promise = registerService.register("user@email.com", "user");
        promise.then(function(result){
            expect(result).toEqual(true);
            expect(messageService.errors).toEqual([]);
        });
        $httpBackend.flush();
    });

    it('should handle a failure', function() {
        $httpBackend.expectPOST('/users/register', {
            "username": "user",
            "password": "user"
        }).respond({ success: false, validationErrors: [{defaultMessage: "invalid email"}] });
        var promise = registerService.register("user", "user");
        promise.then(function(result){
            expect(result).toEqual(false);
            expect(messageService.errors).toEqual(['invalid email']);
        });
        $httpBackend.flush();
    });

    it('should handle an error', function() {
        $httpBackend.expectPOST('/users/register', {
            "username": "user@email.com",
            "password": "user"
        }).respond(500, { success: false, dataErrors: [{defaultMessage: "server down"}] });
        var promise = registerService.register("user@email.com", "user");
        promise.then(function(result){
            expect(result).toEqual(false);
            expect(messageService.errors).toEqual(['server down']);
        });
        $httpBackend.flush();
    });
  });

});
