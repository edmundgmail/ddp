angular.module('sbAdminApp')
.controller('connectionDetailCtrl', ['$scope', '$stateParams', '$http',function($scope, $stateParams,$http){
        $scope.connectionName=$stateParams.connectionName;
        $scope.connectionHierarchies=[{"conn":"my-spark-app","datasource":"cif","dataentity":"transaction","datafield":"name","datatype":"string"}];
        $scope.selectedConnectionHierarchies=null;

        //$scope.dataSources =  $http.get('http://localhost:8881/metadata/connHierarchy?conn='+$scope.connectionName);
        //alert($scope.dataSources);
        //$scope.dataSources=[{"conn":"my-spark-app","datasource":"cif","dataentity":"transaction","datafield":"name","datatype":"string"}];
}]);
