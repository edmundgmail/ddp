'use strict';

angular.module('myApp.ingestion', ['ngRoute','ngResource'])

.factory('copybookIngestion', ['$resource', function($resource) {
return $resource('http://localhost:8881/entity',{},
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

.controller('ingestionCtrl', function($scope, $http) {
    $scope.items = ['copybook', 'csv', 'json', 'jdbc'];
    $scope.selection = $scope.items[0];
    $scope.copybook_fileStructures = ['FixedLength','VariableLength'];
    $scope.copybook_fileStructure = $scope.copybook_fileStructures[0];

    $scope.copybook_binaryFormats = ['FMT_MAINFRAME','FMT_OPEN_COBOL','FMT_INTEL'];
    $scope.copybook_binaryFormat = $scope.copybook_binaryFormats[0];

    $scope.copybook_fontValues = ['cp037'];
    $scope.copybook_fontValue = $scope.copybook_fontValues[0];

    $scope.copybook_splitOptions = ['SplitNone','SplitRedefine','Split01Level','SplitHighestRepeating'];
    $scope.copybook_splitOption = $scope.copybook_splitOptions[0];
    $scope.copybook_cpyBookName="";
    
        $scope.RegisterCopybook = function() {
           var ing = {
              'cpyBookName': $scope.copybook_cpyBookName,
              'cpyBookHdfsPath': $scope.copybook_cpyBookHdfsPath,
              'fileStructure':$scope.copybook_fileStructure, 
              'dataFileHdfsPath': $scope.copybook_dataFileHdfsPath,
              'binaryFormat' : $scope.copybook_binaryFormat,
              'splitOptoin':$scope.copybook_splitOption,
              'cpybookFont':$scope.copybook_fontValue
           }
           //var res = copybookIngestion.update(ing, function(){
           // console.log(res);
           //});

           console.log(ing);
  $http.defaults.headers.common['Accept']= "application/json";
  $http.defaults.headers.common['Content-Type']= "application/json";
  var res = $http.post('http://localhost:8881/entity',ing);

  res.success(function(status, data) {
      console.log(data);
  }).error(function(data) {
      console.log(data);
  });           
        };
  });