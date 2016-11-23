angular.module('sbAdminApp')
.directive('hdfsfileuploader', function() {
    return {
        //restrict: 'E',
        //replace: true,
        scope: {
            uploadedpath : '=',
            uploadedfiles: '=',
            multiple:'@'
          },
        templateUrl:'scripts/directives/hdfsfileuploader/hdfsfileuploader.html',
      controller: function($scope, $rootScope,  $http) {        
        $scope.upload=function(element) {
            var formdata = new FormData();
            
            for( var i=0;  i<element.files.length; i++){
              formdata.append(element.files[i].name, element.files[i]);  
            }
            
            $http.post($rootScope.url+'/file', formdata, {
                  withCredentials: false,
                  transformRequest: angular.identity,
                  headers: {'Content-Type': undefined}})
              .success(function(response){
                    $scope.uploadedpath = response.uploadPath;
                    $scope.uploadedfiles=[];
                    angular.forEach(element.files, function(value, key){$scope.uploadedfiles.push(value.name);});
                    alert('path=' + $scope.uploadedpath);
                    angular.forEach($scope.uploadedfiles, function(value, key){alert('filename=' + value);});
                }).error(function(){
                  alert('error');            
                });
          }; //end of function upload()
        }//end of controler
    }
  });