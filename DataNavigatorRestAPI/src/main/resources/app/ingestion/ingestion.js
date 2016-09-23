'use strict';

angular.module('myApp.ingestion', ['ngRoute','ngResource'])

.factory('copybookIngestion', ['$resource', function($resource) {
return $resource('/entity',{},
    {
        'update': { method:'POST' ,headers: [{'Content-Type': 'application/json'}] },
        'get' : {method: 'GET' }
    });
}])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/ingestion', {
    templateUrl: 'ingestion/ingestion.html',
    controller: 'ingestionCtrl'
  });
}])

.config(function ($httpProvider) {
  $httpProvider.defaults.headers.common = {};
  $httpProvider.defaults.headers.post = {};
  $httpProvider.defaults.headers.put = {};
  $httpProvider.defaults.headers.patch = {};
})
.controller('ingestionCtrl', function($scope, $http) {

    $scope.items = ['copybook', 'csv', 'json', 'jdbc'];
    $scope.selection = $scope.items[0];

    $scope.copybookinfo = {};


    $scope.copybook_fileStructures = ['FixedLength','VariableLength'];
    $scope.copybookinfo.fileStructure = $scope.copybook_fileStructures[0];

    $scope.copybook_binaryFormats = ['FMT_MAINFRAME','FMT_OPEN_COBOL','FMT_INTEL'];
    $scope.copybookinfo.binaryFormat = $scope.copybook_binaryFormats[0];

    $scope.copybook_fontValues = ['cp037'];
    $scope.copybookinfo.fontValue = $scope.copybook_fontValues[0];

    $scope.copybook_splitOptions = ['SplitNone','SplitRedefine','Split01Level','SplitHighestRepeating'];
    $scope.copybookinfo.splitOption = $scope.copybook_splitOptions[0];

        $scope.RegisterCopybook = function() {

           console.log($scope.copybookinfo);
  //$http.defaults.headers.common['Accept']= "application/json";
  $http.defaults.headers.common['Content-Type']= "application/json";
  var res = $http.post('/entity',JSON.stringify($scope.copybookinfo));


        };
  });