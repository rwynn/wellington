/*global angular, $*/
/*jshint undef: true, unused: true, globalstrict:true*/
'use strict';

/* Directives */

var directives = angular.module('wellington.directives', []);

directives.directive('nameValueTable', function () {
    return {
      templateUrl: '/partials/shared/nameValueTable.html',
      transclude: true,
      replace: false,
      scope: {
        pairs: '=',
        title: '@'
      }
    };
});

directives.directive('orderedTable', function () {
    return {
      templateUrl: '/partials/shared/orderedTable.html',
      replace: false,
      scope: {
        elements: '=',
        title: '@'
      }
    };
});

directives.directive('focusMe', function() {
  return {
    link: function(scope, element) {
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
        'prefix': '@',
        'filter': '=value'
      },
      link: function(scope) {
        scope.eventName = function() {
          return [scope.prefix, 'filterChanged'].join('.');
        };
        scope.checkKey = function(e) {
            if (e.keyCode == 13) {
                scope.$emit(scope.eventName(), scope.filter);
            }
        };
        scope.clear = function() {
            scope.filter = "";
            scope.$emit(scope.eventName(), scope.filter);
        };
      }
    };
});

directives.directive('clientSorter', ['$filter', function ($filter) {
    return {
      templateUrl: '/partials/shared/sorter.html',
      replace: true,
      scope: {
        'value': '@',
        'prop' : '@',
        'data': '=',
        'column': '='
      },
      link: function(scope) {
        var orderBy = $filter('orderBy');
        scope.reverse = true;
        scope.$watch('column', function(column) {
          scope.isSorted = (column === scope.value);
          if (!scope.isSorted) {
            scope.reverse = false;
          }
        });
        scope.isSortedAsc = function() {
          return scope.isSorted && !scope.reverse;
        };
        scope.isSortedDesc = function() {
          return scope.isSorted && scope.reverse;
        };
        scope.sort = function() {
          scope.reverse = !scope.reverse;
          scope.data = orderBy(scope.data, scope.prop, scope.reverse);
          scope.column = scope.value;
        };
        if (scope.column === scope.value) {
          scope.sort();
        }
      }
    };
}]);

directives.directive('serverSorter', function () {
    return {
      templateUrl: '/partials/shared/sorter.html',
      replace: true,
      scope: {
        'value': '@',
        'prop': '@',
        'prefix': '@',
        'column': '='
      },
      link: function(scope) {
        var ASC = 'asc',
            DESC = 'desc';
        if (!scope.column.sortDir) {
          scope.column.sortDir = DESC;
        }
        scope.$watch('column.sort', function(column) {
          scope.isSorted = (column === scope.prop);
          if (!scope.isSorted) {
            scope.column.sortDir = DESC;
          }
        });
        scope.isSortedAsc = function() {
          return scope.isSorted && scope.column.sortDir === ASC;
        };
        scope.isSortedDesc = function() {
          return scope.isSorted && scope.column.sortDir === DESC;
        };
        scope.eventName = function() {
          return [scope.prefix, 'sortChanged'].join('.');
        };
        scope.sort = function() {
          scope.column.sort = scope.prop;
          scope.column.sortDir = scope.column.sortDir === DESC ? ASC: DESC;
          scope.$emit(scope.eventName(), scope.column);
        };
      }
    };
});

directives.directive('pager', function () {
    return {
      templateUrl: '/partials/shared/pager.html',
      replace: false,
      scope: {
        state: '=',
        prefix: '@',
        noResults: '@'
      },
      link: function(scope) {
        scope.pagesAvail = function() {
            var i, min, max, avail = [];
            if (!scope.state) {
                return avail;
            }
            if (scope.state.hasPreviousPage) {
                min = -1;
                if (min < (scope.state.pageNumber - 5)) {
                   min = scope.state.pageNumber - 5;
                }
                for (i=scope.state.pageNumber-1; i>min; --i) {
                    avail.unshift(i);
                }
            }
            avail.push(scope.state.pageNumber);
            if (scope.state.hasNextPage) {
                max = scope.state.totalPages;
                if (max > (scope.state.pageNumber + 5)) {
                   max = scope.state.pageNumber + 5;
                }
                for (i=scope.state.pageNumber+1; i<max; ++i) {
                    avail.push(i);
                }
            }
            return avail;
        };
        scope.eventName = function() {
            return [scope.prefix, 'pageChanged'].join('.');
        };
        scope.setPage = function(page) {
            scope.$emit(scope.eventName(), page);
        };
        scope.prevPage = function() {
            scope.$emit(scope.eventName(), scope.state.pageNumber - 1);
        };
        scope.nextPage = function() {
            scope.$emit(scope.eventName(), scope.state.pageNumber + 1);
        };
      }
    };
});

directives.directive('feedbackFormGroup', function () {
    return {
      templateUrl: '/partials/shared/feedbackFormGroup.html',
      replace: true,
      transclude: true,
      scope: {
        field: '='
      },
      link: function(scope) {
        scope.invalid = function() {
            return scope.field && (scope.field.$dirty && scope.field.$invalid);
        };
        scope.valid = function() {
            return !scope.field || scope.field.$pristine || scope.field.$valid;
        };
        scope.feedback = function() {
            return scope.field && scope.field.$dirty;
        };
      }
    };
});

directives.directive('transcludeReplace', function () {
    return {
      link: function($scope, $element, $attrs, controller, $transclude) {
        if ($attrs.ngTransclude === $attrs.$attr.ngTransclude) {
          $attrs.ngTransclude = '';
        }
        function ngTranscludeCloneAttachFn(clone) {
          if (clone.length) {
            $element.replaceWith(clone);
          }
        }
        var slotName = $attrs.ngTransclude || $attrs.ngTranscludeSlot;
        $transclude(ngTranscludeCloneAttachFn, null, slotName);
      }
    };
});
