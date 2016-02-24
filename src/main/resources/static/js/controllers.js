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
    if (!$scope.view) {
        $location.path("/admin/users");
        return;
    }

    $scope.page = parseInt($location.search().page, 10) || 0;
    $scope.filter = $location.search().filter;
    $scope.sortCol = {
      appProps: 'Name',
      javaProps: 'Name',
      users: {
        sort: 'username',
        sortDir: 'asc'
      }
    };

    $scope.initSort = function() {
      if ($location.search().sort) {
        var sortDir = 'asc',
            sort = $location.search().sort;
        if (sort) {
          var sortParams = sort.split(',');
          if (sortParams.length > 0) {
            sort = sortParams[0];
          }
          if (sortParams.length > 1) {
            sortDir = sortParams[1];
          }
        }
        $scope.sortCol.users = {
          sort: sort,
          sortDir: sortDir
        };
      }
    };

    $scope.size = 5;
    $scope.selected = undefined;
    $scope.initSort();

    $scope.loadView = function() {
        var promise;
        if ($scope.view == "users") {
            promise = UsersService.load($scope.page, $scope.size, $scope.filter, $scope.sortCol.users);
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

    $scope.$on('users.pageChanged', function(event, page) {
        $location.search('page', page);
    });

    $scope.$on('users.filterChanged', function(event, filter) {
        $location.search('filter', filter);
    });

    $scope.$on('users.sortChanged', function(event, column) {
        $location.search('sort', [column.sort, column.sortDir].join(','));
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
