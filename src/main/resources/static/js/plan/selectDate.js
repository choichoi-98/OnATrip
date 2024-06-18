$(document).ready(function(){
        // moment.js 한국어 설정 로드
            moment.locale('ko');

            $('#datepicker-container').daterangepicker({
                autoUpdateInput: false,
                alwaysShowCalendars: true,
                minDate: moment(),
                locale: {
                    cancelLabel: '지우기',
                    applyLabel: '적용',
                    customRangeLabel: '사용자 지정',
                    daysOfWeek: ['일', '월', '화', '수', '목', '금', '토'],
                    monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
                    firstDay: 0,
                    format: 'YYYY/MM/DD',
                    separator: ' - ',
                    applyButtonClasses: 'btn-primary',
                    cancelButtonClasses: 'btn-secondary'
                }
            });

         // apply event
         $('#datepicker-container').on('apply.daterangepicker', function(ev, picker) {
             $(this).val(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('MM/DD/YYYY'));
         });

         // cancel event
         $('#datepicker-container').on('cancel.daterangepicker', function(ev, picker) {
             $(this).val('');
         });

         $('#datepicker-container').on('click', function() {
             $(this).trigger('click');
         });

         //---완료 버튼 클릭 시
         $('.btn-get-started').on('click',function(){
            var dateRange = $('#datepicker-container').val();
            console.log(dateRange);

            if(!dateRange){

                $('#datepicker-container').focus();
            }else{
                // URL 생성
                var url = '../detailPlan?dateRange=' + encodeURIComponent(dateRange);
                // 페이지 이동
                window.location.href = url;
            }

         });//$('.btn-get-started').on('click',function(){

});//$(document).ready(function(){
