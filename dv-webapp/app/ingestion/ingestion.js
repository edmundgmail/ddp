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

.controller('ingestionCtrl', function($scope, copybookIngestion) {
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

        $scope.RegisterCopybook = function() {
           console.log('User clicked register', $scope.copybook_fileStructure);
           var ing = {
              'cpyBookName': $scope.copybook_cpyBookName,
              'cpyBookHdfsPath': $scope.cpyBookHdfsPath
              // \"/user/root/LRPWSACT.cpy\", \"fileStructure\":\"FixedLength\", \"binaryFormat\": \"FMT_MAINFRAME\", \"splitOptoin\": \"SplitNone\", \"dataFileHdfsPath\":\"/user/root/RPWACT.FIXED.END\", \"cpybookFont\":\"cp037\" }"

           }
           var res = copybookIngestion.get({}, function(){
            console.log(res);
           });
        };
  });