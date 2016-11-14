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
.controller('newEntityCtrl', ['$scope', '$stateParams', '$http',function($scope, $stateParams,$http){
        $scope.FileTypes=['cpybook', 'csv', 'xml'];
        $scope.CopybookSplitLevels = [ {'id': 0,'name':'No Split'}, {'id': 1, 'name':'On Redefine'}, {'id': 2,'name':'On 01 Level'},{'id': 3,'name':'On Highest Repeating'}];
        $scope.CopybookFileStructure = [{'id': 2, 'name':'Fixed Length'}];
        $scope.CopybookBnaryFormat = [{'id': 1, 'name':'Mainframe'}];
        $scope.CopybookFont = ['cp037'];

        $scope.PreviewCopybookFile=function(){
            alert($scope.selectCopybookSplitLevel.name);
            alert($scope.selectCopybookFileStructure.name);
            alert($scope.selectCopybookBinaryFormat.name);
            alert($scope.selectCopybookFont);
            var formdata = new FormData();
            formdata.append('copybook', $scope.cpybookfile);

            $http.post('http://192.168.56.101:8881/file', formdata, {
                  withCredentials: false,
                  transformRequest: angular.identity,
                  headers: {'Content-Type': undefined}}).success(function(){
              alert('success');
            }).error(function(){
              alert('error');
            });
            //"{ \"cpyBookName\": \"RPWACT\", \"cpyBookHdfsPath\": \"/user/root/LRPWSACT.cpy\", \"fileStructure\":\"FixedLength\", \"binaryFormat\": \"FMT_MAINFRAME\", \"splitOptoin\": \"SplitNone\", \"dataFileHdfsPath\":\"/user/root/RPWACT.FIXED.END\", \"cpybookFont\":\"cp037\" }"
            
        };

}]);