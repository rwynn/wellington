/*global angular, $*/
/*jshint undef: true, unused: true*/
'use strict';

/* Services */

var services = angular.module('wellington.services', []);

services.factory('RegisterService', ['$http', '$q', 'MessageService', function($http, $q, MessageService) {
    var self = {};
    self.register = function(username, password) {
        var deferred = $q.defer();
        var payload = { username: username, password: password };
        $http.post('/users/register', payload).
        success(function(data, status, headers, config){
            MessageService.parseResponse(data);
            deferred.resolve(data.success);
        }).
        error(function(data, status, headers, config){
            MessageService.parseResponse(data);
            deferred.resolve(false);
        });
        return deferred.promise;
    };
    return self;
}]);

services.factory('AuthService', ['$http', '$q', 'MessageService', function($http, $q, MessageService) {
    var self = {};
    self.authorities = undefined;
    self.username = undefined;
    self.ROLE_ADMIN = 'ROLE_ADMIN';
    self.load = function() {
        var deferred = $q.defer();
        if (self.authorities) {
           deferred.resolve(self.authorities);
        } else {
            $http.get('/users/auth').
            success(function(data, status, headers, config){
                if (data.success) {
                    self.authorities = data.results.authorities;
                    self.username = data.results.username;
                }
                MessageService.parseResponse(data);
                deferred.resolve(self.authorities);
            }).
            error(function(data, status, headers, config){
                MessageService.parseResponse(data);
                deferred.resolve(null);
            });
        }
        return deferred.promise;
    };
    self.isAdmin = function(authorities) {
        return authorities.indexOf(self.ROLE_ADMIN) >= 0;
    };
    self.isLoggedInAs = function(username) {
        return self.username == username;
    };
    return self;
}]);

services.factory('IsAdmin', ['$q', 'AuthService', function($q, AuthService) {
    var deferred = $q.defer();
    var self = deferred.promise;
    var promise = AuthService.load();
    promise.then(function(authorities) {
        if (authorities && AuthService.isAdmin(authorities)) {
            deferred.resolve(true);
        } else {
            deferred.reject();
        }
    });
    return self;
}]);

services.factory('EnvService', ['$http', '$q', 'MessageService', function($http, $q, MessageService) {
    var self = {};
    self.load = function() {
        var deferred = $q.defer();
        if (self.result) {
            deferred.resolve(self.result);
        } else {
            $http.get("/env").
            success(function(data, status, headers, config){
                var result = { classpath: [], appProps: [], javaProps: [] };
                var classpath = data.systemProperties['java.class.path'];
                for (var key in data) {
                    if (data.hasOwnProperty(key)
                        && key.indexOf("applicationConfig") == 0) {
                        var nextConfig = data[key];
                        for (var key2 in nextConfig) {
                            if (nextConfig.hasOwnProperty(key2)) {
                                result.appProps.push({name: key2, value: nextConfig[key2] });
                            }
                        }
                    }
                }
                for (var key in data.systemProperties) {
                    if (key == "java.class.path") continue;
                    if (key.indexOf("java.") == 0 && data.systemProperties.hasOwnProperty(key)) {
                        result.javaProps.push({name: key, value: data.systemProperties[key] });
                    }
                }
                result.classpath = classpath.split(data.systemProperties['path.separator']);
                self.result = result;
                deferred.resolve(result);
            }).
            error(function(data, status, headers, config){
                MessageService.addError("Unable to retrieve environment information");
                deferred.resolve(null);
            });
        }
        return deferred.promise;
    };
    return self;
}]);

services.factory('MetricsService', ['$http', '$q', 'MessageService', function($http, $q, MessageService) {
    var self = {};
    self.load = function() {
        var deferred = $q.defer();
        $http.get("/metrics").
        success(function(data, status, headers, config){
            var metrics = { response: [], counter: [] };
            for (var key in data) {
                if (data.hasOwnProperty(key)) {
                    if (key.indexOf("gauge.response") == 0) {
                        metrics.response.push({name: key, value: data[key] });
                    } else if (key.indexOf("counter") == 0) {
                        metrics.counter.push({name: key, value: data[key] });
                    } else {
                        metrics[key] = data[key];
                    }
                }
            }
            deferred.resolve(metrics);
        }).
        error(function(data, status, headers, config){
            MessageService.addError("Unable to retrieve metrics");
            deferred.resolve(null);
        });
        return deferred.promise;
    };
    return self;
}]);

services.factory('UsersService', ['$http', '$q', 'MessageService', function($http, $q, MessageService) {
    var self = {};
    self.load = function(page, size, filter) {
        var path = '/users/admin/list';
        var args = {};
        if (page) {
            args.page = page;
        }
        if (size) {
            args.size = size;
        }
        if (filter) {
            args.filter = filter;
        }
        path = path + '?' + $.param(args);
        var deferred = $q.defer();
        $http.get(path).
        success(function(data, status, headers, config){
            MessageService.parseResponse(data);
            deferred.resolve(data);
        }).
        error(function(data, status, headers, config){
            MessageService.parseResponse(data);
            deferred.resolve(null);
        });
        return deferred.promise;
    };
    self.updateAuthorities = function(username, authorities) {
        var payload = { username: username, authorities: authorities};
        var deferred = $q.defer();
        $http.post('/users/admin/roles', payload).
        success(function(data, status, headers, config){
         MessageService.parseResponse(data);
         deferred.resolve(data.success);
        }).
        error(function(data, status, headers, config){
         MessageService.parseResponse(data);
         deferred.resolve(false);
        });
        return deferred.promise;
    };
    self.updateLock = function(username, locked) {
        var payload = { username: username, locked: locked};
        var deferred = $q.defer();
        $http.post('/users/admin/lock', payload).
        success(function(data, status, headers, config){
            MessageService.parseResponse(data);
            deferred.resolve(data.success);
        }).
        error(function(data, status, headers, config){
            MessageService.parseResponse(data);
            deferred.resolve(false);
        });
        return deferred.promise;
    };
    return self;
}]);

services.factory('MessageService', [function() {
    var self = {};
    self.errors = [];
    self.success = [];
    self.clear = function() {
        self.errors.splice(0);
        self.success.splice(0);
    };
    self.addSuccess = function(msg) {
        self.success.push(msg);
    };
    self.addError = function(error) {
        self.errors.push(error);
    };
    self.parseResponse = function(resp) {
        self.clear();
        if (resp.success == false) {
            if (resp.validationErrors) {
                angular.forEach(resp.validationErrors, function(value, key) {
                   self.errors.push(value.defaultMessage);
                });
            }
            if (resp.dataErrors) {
                angular.forEach(resp.dataErrors, function(value, key) {
                    self.errors.push(value.defaultMessage);
                });
            }
        }
    };
    return self;
}]);