'use strict';

// Declare app level module which depends on filters, and services
angular.module('wellington', [
  'ngRoute',
  'ngAnimate',
  'wellington.filters',
  'wellington.services',
  'wellington.directives',
  'wellington.controllers'
]).
config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/home', {templateUrl: '/partials/home.html'});
    $routeProvider.when('/register', {templateUrl: '/partials/register.html', controller: 'Register'});
    $routeProvider.when('/registered', {templateUrl: '/partials/registered.html'});
    $routeProvider.when('/admin/:view?/:page?/:filter?',
        {templateUrl: '/partials/admin.html', controller: 'Admin', resolve: { "IsAdmin": "IsAdmin" }});
    $routeProvider.otherwise({redirectTo: '/home'});
}]).
run(['$rootScope', '$location', function($rootScope, $location) {
    $rootScope.$on("$routeChangeSuccess", function () {
        $('.navItem').removeClass("active").each(function() {
           var prefix = $("a", this).attr('href').substring(1);
           if ($location.path().indexOf(prefix) == 0) {
                $(this).addClass("active");
           }
        });
    });
}]);