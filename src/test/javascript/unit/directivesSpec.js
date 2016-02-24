'use strict';
/* jasmine specs for directives go here */
describe('nameValueTable', function() {
  var elm, scope, compile;
  beforeEach(module('templates'));
  beforeEach(module('wellington.directives'));
  beforeEach(inject(function($rootScope, $compile) {
      scope = $rootScope;
      compile = $compile;
  }));
  it('should display a table sorted by name', function() {
    scope.env = {
      javaProps: [{ name: "version", value: "1.7" }, {name: "home", value: "/home/wellington"}]
    };
    scope.sortCol = {
      javaProps: 'Name'
    };
    elm = angular.element("<div name-value-table='true' title='Java Properties' pairs='env.javaProps'><div client-sorter='true' prop='name' value='Name' data='env.javaProps' column='sortCol.javaProps'></div><div client-sorter='true' prop='value' value='Value' data='env.javaProps' column='sortCol.javaProps'></div></div>");
    compile(elm)(scope);
    scope.$digest();
    var title = elm.find("h3");
    var head = elm.find("thead");
    var body = elm.find("tbody");
    var rows = body.find("tr");
    var firstRow = rows.eq(0);
    var secondRow = rows.eq(1);
    expect(title.text()).toBe("Java Properties");
    expect(head.find("td").eq(0).text().trim()).toBe("Name");
    expect(head.find("td").eq(1).text().trim()).toBe("Value");
    expect(rows.length).toBe(2);
    expect(firstRow.find("td").eq(0).text()).toBe("home");
    expect(firstRow.find("td").eq(1).text()).toBe("/home/wellington");
    expect(secondRow.find("td").eq(0).text()).toBe("version");
    expect(secondRow.find("td").eq(1).text()).toBe("1.7");
  });
});

describe('orderedTable', function() {
  var elm, scope, compile;
  beforeEach(module('templates'));
  beforeEach(module('wellington.directives'));
  beforeEach(inject(function($rootScope, $compile) {
      scope = $rootScope;
      compile = $compile;
  }));
  it('should display a table or ordered elements', function() {
    scope.env = {
        letters: ["a", "b", "c"]
    };
    elm = angular.element('<div ordered-table="true" title="Letters" elements="env.letters" />');
    compile(elm)(scope);
    scope.$digest();
    var title = elm.find("h3");
    var head = elm.find("thead");
    var body = elm.find("tbody");
    var rows = body.find("tr");
    var firstRow = rows.eq(0);
    var secondRow = rows.eq(1);
    var thirdRow = rows.eq(2);
    expect(title.text()).toBe("Letters");
    expect(head.find("td").eq(0).text()).toBe("Position");
    expect(head.find("td").eq(1).text()).toBe("Element");
    expect(rows.length).toBe(3);
    expect(firstRow.find("td").eq(0).text()).toBe("1");
    expect(firstRow.find("td").eq(1).text()).toBe("a");
    expect(secondRow.find("td").eq(0).text()).toBe("2");
    expect(secondRow.find("td").eq(1).text()).toBe("b");
    expect(thirdRow.find("td").eq(0).text()).toBe("3");
    expect(thirdRow.find("td").eq(1).text()).toBe("c");
  });
});
