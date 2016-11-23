'use strict';

/**
 * @ngdoc directive
 * @name izzyposWebApp.directive:adminPosHeader
 * @description
 * # adminPosHeader
 */
angular.module('sbAdminApp')
	.directive('newentity',function(){
		return {
        templateUrl:'scripts/directives/newentity/newentity.html',
        scope: {
        	'datasource':'@datasource'
          'connectionName':'@'
      	}
    	}
	});

angular.module('sbAdminApp')
  .directive('cpybooknewentity', function(){
      return {
        templateUrl:'scripts/directives/newentity/cpybooknewentity.html',
      }
  });

angular.module('sbAdminApp')
.controller('newEntityCtrl', ['$scope','$rootScope', '$stateParams', '$http',function($scope, $rootScope, $stateParams,$http){
        $scope.FileTypes=['cpybook', 'csv', 'xml'];
        $scope.CopybookSplitLevels = [ {'id': 0,'name':'No Split'}, {'id': 1, 'name':'On Redefine'}, {'id': 2,'name':'On 01 Level'},{'id': 3,'name':'On Highest Repeating'}];
        $scope.CopybookFileStructure = [{'id': 2, 'name':'Fixed Length'}];
        $scope.CopybookBnaryFormat = [{'id': 1, 'name':'Mainframe'}];
        $scope.CopybookFont = ['cp037'];

        $scope.uploadedCopybookFiles=[];
        $scope.uploadedCopybookPath=null;
        $scope.uploadedCopybookDataPath=null;
        $scope.uploadedCopybookDataFiles=[];
        $scope.selectCopybookSplitLevel=null;
        $scope.selectCopybookFileStructure=null;
        $scope.selectCopybookBinaryFormat=null;
        $scope.selectCopybookFont=null;

        $scope.PreviewCopybookFile = function(){
              alert('Please input all the fields');

              var formData = {
                               'conn' : $scope.connectionName,
                               'cpyBookName' : $scope.uploadedCopybookFiles[0],
                               'cpyBookHdfsPath' : $scope.uploadedCopybookPath+'/'+ $scope.uploadedCopybookFiles[0],
                               'dataFileHdfsPath': $scope.uploadedCopybookDataPath+$scope.uploadedCopybookDataFiles[0],
                               'cpybookFont': $scope.selectCopybookFont,
                               'fileStructure': $scope.selectCopybookFileStructure,
                               'binaryFormat': $scope.selectCopybookBinaryFormat,
                               'splitOptoin': $scope.selectCopybookFont
                  };   
                $http.post($rootScope.url+'/entity', formdata, {
                  withCredentials: false,
                  transformRequest: angular.identity,
                  headers: {'Content-Type': 'application/json'}})
              .success(function(response){
                 alert('success');
                }).error(function(){
                  alert('error');            
                });

          };

}]);