'use strict';

// for more info on element api
// see https://github.com/angular/protractor/blob/master/spec/basic/findelements_spec.js

describe('Wellington', function() {

    var expectMessage = function(isError, msg) {
        var selector = isError ? ".alert-danger": ".alert-success";
        expect(element(by.css(selector)).getText()).toEqual(msg);
    };

    var expectError = function(msg) {
        return expectMessage(true, msg);
    };

    var expectSuccess = function(msg) {
        return expectMessage(false, msg);
    }

    var logout = function() {
        element(by.css("a[href='/logout']")).click();
        expectSuccess("You have successfully logged out");
    };

    var gotoAdminTab = function() {
        element(by.css("a[href='#/admin']")).click();
    };

    var gotoPage = function(page) {
        element(by.css("a[title='Page " + page + "']")).click();
    }

    var expectTabs = function(expectedTabs) {
        element.all(by.css(".navItem")).then(function(tabs) {
            expect(tabs.length).toEqual(expectedTabs.length);
            for (var i=0, j=tabs.length; i<j; ++i) {
                expect(tabs[i].getText()).toEqual(expectedTabs[i]);
            }
        });
    };

    var expectPills = function(expectedPills) {
        element.all(by.css("ul.nav-pills li a")).then(function(pills) {
            expect(pills.length).toEqual(expectedPills.length);
            for (var i=0, j=pills.length; i<j; ++i) {
                expect(pills[i].getText()).toEqual(expectedPills[i]);
            }
        });
    };

    var login = function(username, password) {
        var user = element(by.id("username"));
        var pass = element(by.id("password"));
        expect(user.isPresent());
        expect(pass.isPresent());
        user.sendKeys(username);
        pass.sendKeys(password);
        element(by.buttonText("Sign in")).click();
    };

    var register = function(username, password) {
        element(by.css("a[href='#/register']")).click();
        var user = element(by.id("regUsername"));
        var pass = element(by.id("regPassword"));
        var submit = element(by.buttonText("Submit"));
        expect(!submit.isEnabled());
        expect(user.isPresent());
        expect(pass.isPresent());
        user.sendKeys(username);
        pass.sendKeys(password);
        expect(submit.isEnabled());
        submit.click();
    };

    var expectUsers = function(expectedUsers) {
        element.all(by.repeater("u in users.content")).then(function(users) {
            expect(users.length).toEqual(expectedUsers);
        });
    };

    var expectPages = function(current, total) {
        var showing = element(by.css("span.pagination-showing"));
        expect(showing.getText()).toEqual("Showing page " + current + " of " + total);
    }

    var filterUsers = function(filterText) {
        var filter = element(by.model("filter"));
        filter.sendKeys(filterText);
        filter.sendKeys(protractor.Key.ENTER);
    };

    browser.get('/');

    it('should automatically redirect to /login before you have logged in', function() {
        expect(browser.getLocationAbsUrl()).toMatch("/login");
    });


    describe('admin login feature', function() {

        beforeEach(function() {
            browser.get('/');
            // no tabs before login
            expectTabs([]);
        });


        it('should show allow a successful login', function() {
            login('admin', 'admin');
            expectTabs(["Home", "Admin"]);
            logout();

        });

        it('should fail on invalid username', function() {
            login('foo', 'admin');
            expectError("Wrong user or password");
        });

        it('should fail on invalid password', function() {
            login('admin', 'foo');
            expectError("Wrong user or password");
        });

    });

    describe('admin feature', function() {

        beforeEach(function() {
            browser.get('/');
            login('admin', 'admin');
        });

        it('should show admin functions', function() {
            gotoAdminTab();
            expectPills(['Users', 'Metrics', 'Environment']);
            logout();
        });

    });

    describe('register feature', function() {

        beforeEach(function() {
            browser.get('/');
        });

        it('should allow a user to register using email and password', function() {
            register("user@email.com", "T1gB3!xQuX");
            expectSuccess("Registration completed");
        });


        it('should only allow a user to register once', function() {
            register("user@email.com", "T1gB3!xQuX");
            expectError("User already exists");
        });

        it('should not allow weak passwords', function() {
            register("weak@email.com", "123456789");
            expectError("Please choose a stronger password. Your password should be between 8 and 16 characters and contain a mix of digits and letters.");
        });
    });

    describe('user login feature', function() {

        beforeEach(function() {
            browser.get('/');
            // no tabs before login
            expectTabs([]);
        });


        it('should show allow a successful login', function() {
            login('user@email.com', 'T1gB3!xQuX');
            expectTabs(["Home"]);
            logout();
        });

    });

    describe('paginate users feature', function() {

        beforeEach(function() {
            browser.get('/');
        });

        it('should allow pagination of system users', function() {
            for (var i=0; i<20; ++i) {
                register("user" + i + "@email.com", "T1gB3!xQuX");
            }
            login('admin', 'admin');
            expectTabs(["Home", "Admin"]);
            gotoAdminTab();
            expectPages(1, 5);
            expectUsers(5);
            gotoPage(2);
            expectPages(2, 5);
            expectUsers(5);
            gotoPage(3);
            expectPages(3, 5);
            expectUsers(5);
            gotoPage(4);
            expectPages(4, 5);
            expectUsers(5);
            gotoPage(5);
            expectPages(5, 5);
            expectUsers(2);
            logout();
        });

    });

    describe('filter users feature', function() {

        beforeEach(function() {
            browser.get('/');
        });

        it('should allow filtering of system users', function() {
            login('admin', 'admin');
            expectTabs(["Home", "Admin"]);
            gotoAdminTab();
            filterUsers("adm");
            expectPages(1, 1);
            expectUsers(1);
            logout();
        });

    });


});
