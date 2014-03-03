/*global angular, $*/
/*jshint undef: true, unused: true*/
'use strict';

/* Directives */

var directives = angular.module('wellington.directives', []);

directives.directive('nameValueTable', function () {
    return {
      templateUrl: '/partials/shared/nameValueTable.html',
      replace: false,
      scope: {
        pairs: '=',
        title: '@'
      }
    }
});

directives.directive('orderedTable', function () {
    return {
      templateUrl: '/partials/shared/orderedTable.html',
      replace: false,
      scope: {
        elements: '=',
        title: '@'
      }
    }
});

directives.directive('focusMe', function() {
  return {
    link: function($scope, element, attrs) {
      $(element).focus();
    }
  };
});

directives.directive('filter', function () {
    return {
      templateUrl: '/partials/shared/filter.html',
      replace: false,
      scope: {
        'placeholder': '@',
        'filter': '=value'
      },
      link: function($scope, element, attrs) {
        $scope.checkKey = function(e) {
            if (e.keyCode == 13) {
                $scope.$emit('filterChanged', $scope.filter);
            }
        };
        $scope.clear = function() {
            $scope.filter = "";
            $scope.$emit('filterChanged', $scope.filter);
        };
      }
    }
});

directives.directive('pager', function () {
    return {
      templateUrl: '/partials/shared/pager.html',
      replace: false,
      scope: {
        state: '=',
        noResults: '@'
      },
      link: function($scope, element, attrs) {
        $scope.pagesAvail = function() {
            var avail = [];
            if (!$scope.state) {
                return avail;
            }
            if ($scope.state.hasPreviousPage) {
                var min = -1;
                if (min < ($scope.state.pageNumber - 5)) {
                   min = $scope.state.pageNumber - 5;
                }
                for (var i=$scope.state.pageNumber-1; i>min; --i) {
                    avail.unshift(i);
                }
            }
            avail.push($scope.state.pageNumber);
            if ($scope.state.hasNextPage) {
                var max = $scope.state.totalPages;
                if (max > ($scope.state.pageNumber + 5)) {
                   max = $scope.state.pageNumber + 5;
                }
                for (var i=$scope.state.pageNumber+1; i<max; ++i) {
                    avail.push(i);
                }
            }
            return avail;
        };
        $scope.setPage = function(page) {
            $scope.$emit('pageChanged', page);
        };
        $scope.prevPage = function() {
            $scope.$emit('pageChanged', $scope.state.pageNumber - 1);
        };
        $scope.nextPage = function() {
            $scope.$emit('pageChanged', $scope.state.pageNumber + 1);
        };
      }
    }
});