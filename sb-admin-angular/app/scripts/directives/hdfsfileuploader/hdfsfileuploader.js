angular.module('sbAdminApp')
.directive('hdfsfileuploader', function() {
    return {
        //restrict: 'E',
        //replace: true,
        scope: {
            uploadedfiles: '=',
            multiple:'@'
        },
        templateUrl:'scripts/directives/hdfsfileuploader/hdfsfileuploader.html',
      controller: function($scope) {        
        $scope.upload=function(element) {
          console.log('from directive', $scope.multiple); 
          console.log('file=', element.files[0].name);  
        };
      }
    }
});