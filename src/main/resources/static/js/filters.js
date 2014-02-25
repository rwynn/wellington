'use strict';

/* Filters */

var filters = angular.module('wellington.filters', []);

filters.filter('resource', function() {
    return function(key) {
        if (key.indexOf("gauge.response.") == 0) {
            return key.substring("gauge.response.".length);
        } else if (key.indexOf("counter.status.") == 0) {
            var reg = /counter\.status\.([0-9]+)\.*/g;
            var matches = reg.exec(key);
            return key.substring(matches[0].length);
        } else {
            return key;
        }
    };
});

filters.filter('responseCode', function() {
    return function(key) {
        if (key.indexOf("counter.status.") == 0) {
            var reg = /counter\.status\.([0-9]+)\.*/g;
            var matches = reg.exec(key);
            return matches[1];
        } else {
            return key;
        }
    };
});