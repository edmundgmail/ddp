angular.module('sbAdminApp')
.controller('connectionDetailCtrl', ['$scope', '$stateParams', '$http',function($scope, $stateParams,$http){
        $scope.connectionName=$stateParams.connectionName;
        $scope.dataSources =  $http.get('http://localhost:8881/metadata/dataSources?'+$scope.connectionName);
        alert($scope.dataSources);
}]);
