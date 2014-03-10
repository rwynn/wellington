module.exports = function(config){
    config.set({
    basePath : '../../../',

    files : [
      'src/test/javascript/unit/angular/angular.js',
      'src/test/javascript/unit/angular/angular-*.js',
      'src/main/resources/static/js/**/*.js',
      'src/test/javascript/unit/*.js'
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
            'karma-jasmine'
            ],

    junitReporter : {
      outputFile: 'build/test-results/karma.xml',
      suite: 'unit'
    }

})}
