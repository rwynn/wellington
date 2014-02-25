exports.config = {
  allScriptsTimeout: 11000,

  specs: [
    'e2e/*.js'
  ],

  capabilities: {
    'browserName': 'chrome'
  },

  baseUrl: 'http://localhost:8080',

  framework: 'jasmine',

  onPrepare: function() {
      // The require statement must be down here, since jasmine-reporters
      // needs jasmine to be in the global and protractor does not guarantee
      // this until inside the onPrepare function.
      require('jasmine-reporters');
      jasmine.getEnv().addReporter(
        new jasmine.JUnitXmlReporter('build/test-results/', true, true));
  },

  jasmineNodeOpts: {
    defaultTimeoutInterval: 90000
  }
};