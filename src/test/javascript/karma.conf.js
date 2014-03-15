module.exports = function(config){
    config.set({
    basePath : '../../../',

    preprocessors: {
        'src/main/resources/static/partials/**/*.html': ['ng-html2js']
    },

    ngHtml2JsPreprocessor: {
          stripPrefix: 'src/main/resources/static',
          moduleName: 'templates'
    },

    files : [
      'src/test/javascript/unit/angular/angular.js',
      'src/test/javascript/unit/angular/angular-*.js',
      'src/main/resources/static/js/**/*.js',
      'src/test/javascript/unit/*.js',
      'src/main/resources/static/partials/**/*.html'
    ],

    exclude : [
      'src/test/javascript/unit/angular/angular-loader.js',
      'src/test/javascript/unit/angular/*.min.js',
      'src/test/javascript/unit/angular/angular-scenario.js'
    ],

    autoWatch : true,

    frameworks: ['jasmine'],

    browsers : ['Chrome'],

    plugins : [
            'karma-junit-reporter',
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine',
            'karma-ng-html2js-preprocessor'
            ],

    junitReporter : {
      outputFile: 'build/test-results/karma.xml',
      suite: 'unit'
    }

})}
