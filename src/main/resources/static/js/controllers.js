/*global angular, $*/
/*jshint undef: true, unused: true, globalstrict:true*/
'use strict';

/* Controllers */

var controllers = angular.module('wellington.controllers', []);

controllers.controller('Register', ['$scope', '$location', '$route', 'RegisterService', 'MessageService',
    function($scope, $location, $route, RegisterService, MessageService) {

    $scope.form = { username: null, password: null };

    $scope.register = function() {
       var promise = RegisterService.register($scope.form.username, $scope.form.password);
       promise.then(function(result) {
          if (result) {
            MessageService.addSuccess("Registration completed");
            $location.path("/registered");
          }
       });
    };

    $scope.resetMessages = function() {
        MessageService.clear();
    };

    $scope.cancel = function() {
        $location.path("/");
    };

}]);

controllers.controller('Admin', ['$scope', '$routeParams', '$location', 'UsersService', 'MetricsService', 'EnvService', 'MessageService', 'IsAdmin', 'AuthService',
    function($scope, $routeParams, $location, UsersService, MetricsService, EnvService, MessageService, IsAdmin, AuthService) {

    $scope.view = $routeParams.view;
    $scope.page = parseInt($routeParams.page, 10) || 0;
    $scope.filter = $routeParams.filter;

    $scope.size = 5;
    $scope.selected = undefined;

    if (!$scope.view) {
        $location.path("/admin/users");
        return;
    }

    $scope.loadView = function() {
        var promise;
        if ($scope.view == "users") {
            promise = UsersService.load($scope.page, $scope.size, $scope.filter);
            promise.then(function(result) {
                if (result) {
                    $scope.users = result.results;
                }
            });
        } else if ($scope.view == "metrics") {
            promise = MetricsService.load();
            promise.then(function(result) {
                if (result) {
                    $scope.metrics = result;
                }
            });
        } else if ($scope.view == "env") {
            promise = EnvService.load();
            promise.then(function(result) {
                if (result) {
                    $scope.env = result;
                }
            });
        }
    };

    $scope.edit = function(user) {
        $scope.selected = user;
    };

    $scope.$on('pageChanged', function(event, page) {
        // when the pager updates the page then update route
        if ($scope.filter) {
            $location.path("/admin/users/" + page + "/" + $scope.filter);
        } else {
            $location.path("/admin/users/" + page);
        }

    });

    $scope.$on('filterChanged', function(event, filter) {
        $scope.filter = filter;
        if ($scope.filter) {
            $location.path("/admin/users/0/" + encodeURIComponent($scope.filter));
        } else {
            $location.path("/admin/users/");
        }
    });

    $scope.isSelectedAdmin = function() {
        return AuthService.isAdmin($scope.selected.authorities);
    };

    $scope.isSelectedLocked = function() {
        return $scope.selected.locked;
    };

    $scope.isSelf = function() {
        return AuthService.isLoggedInAs($scope.selected.username);
    };

    $scope.cancelEdit = function() {
        $scope.selected = undefined;
    };

    $scope.toggleAdmin = function() {
        var promise;
        if (!AuthService.isAdmin($scope.selected.authorities)) {
            promise = UsersService.updateAuthorities($scope.selected.username,
                [AuthService.ROLE_ADMIN]);
            promise.then(function(result) {
                if (result) {
                    $scope.selected = undefined;
                    $scope.loadView();
                }
            });
        }
    };

    $scope.toggleLock = function() {
        var promise;
        promise = UsersService.updateLock($scope.selected.username,
            !$scope.selected.locked);
        promise.then(function(result) {
            if (result) {
                $scope.selected = undefined;
                $scope.loadView();
            }
        });
    };

    $scope.memFree = function() {
        if ($scope.metrics) {
            return { "width": ($scope.metrics['mem.free'] / $scope.metrics.mem)*100 + "%" };
        }
        return {"width": "0%"};
    };

    $scope.memUsed = function() {
        if ($scope.metrics) {
            return { "width": (1 - ($scope.metrics['mem.free'] / $scope.metrics.mem))*100 + "%" };
        }
        return {"width": "0%"};
    };

    $scope.loadView();

}]);

controllers.controller('Messages', ['$scope', 'MessageService', function($scope, MessageService) {
    $scope.errors = MessageService.errors;
    $scope.success = MessageService.success;
    $('#serverMessages').remove();
}]);