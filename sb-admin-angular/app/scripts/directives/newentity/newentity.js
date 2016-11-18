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

        $scope.UploadCopybookFile=function(element){
            var formdata = new FormData();
            formdata.append(element.files[0].name, element.files[0]);
            $http.post($rootScope.url+'/file', formdata, {
                  withCredentials: false,
                  transformRequest: angular.identity,
                  headers: {'Content-Type': undefined}}).success(function(response){
                  $scope.copybookfileUploadPath = response.uploadPath + "/" + element.files[0].name;
                  alert('filename=' + $scope.copybookfileUploadPath);
            }).error(function(){
              alert('error');
            });
          };

         $scope.UploadCopybookDataFiles=function(element){
            //alert($scope.selectCopybookSplitLevel.name);
            //alert($scope.selectCopybookFileStructure.name);
            //alert($scope.selectCopybookBinaryFormat.name);
            //alert($scope.selectCopybookFont);
            var formdata = new FormData();
            
            for( var i=0;  i<element.files.length; i++){
              formdata.append(element.files[i].name, element.files[i]);  
            }
            
            $http.post($rootScope.url+'/file', formdata, {
                  withCredentials: false,
                  transformRequest: angular.identity,
                  headers: {'Content-Type': undefined}}).success(function(response){
                  $scope.cpybookdatafilesUploadPath = response.uploadPath;
                  $scope.cpybookdatafileUploadFileNames=[];
                  angular.forEach(element.files, function(item){$scope.cpybookdatafileUploadFileNames.push(item.name);});
                  alert('path=' + $scope.cpybookdatafilesUploadPath);
                  angular.foreach($scope.cpybookdatafileUploadFileNames, function(item){alert('filename=' + item);});
            }).error(function(){
              alert('error');            
             });
        };

        $scope.PreviewCopybookFile = function(){
          if(!$scope.selectCopybookFont 
            || !$scope.selectCopybookBinaryFormat 
            || !$scope.selectCopybookFileStructure 
            || !$scope.selectCopybookSplitLevel 
            || !$scope.cpybookdatafilesUploadPath 
            || !$scope.copybookfileUploadPath )
              alert('Please input all the fields');
          };

}]);