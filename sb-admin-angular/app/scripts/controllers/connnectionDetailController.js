angular.module('sbAdminApp')
.controller('connectionDetailCtrl', ['$scope', '$stateParams', '$http',function($scope, $stateParams,$http){
        $scope.connectionName=$stateParams.connectionName;
        $scope.connectionHierarchies=[];
        $scope.selectedDataEntities=[];
        $http.get('http://localhost:8881/metadata/connHierarchy?conn='+$scope.connectionName)
         .then(function(response) {
          $scope.connectionHierarchies = response.data;

        });

$scope.getSelectedDataEntities=function(){
        if($scope.selectedDataSource !=null){
         	var key = $scope.connectionHierarchies.indexOf($scope.selectedDataSource);
         	$scope.selectedDataEntities = $scope.connectionHierarchies[key].dataEntities;         	
         }	
}

}]);