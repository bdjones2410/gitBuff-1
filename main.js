(function() {
    $(document).ready(function() {
        page.init();
    });

    page = {
        userLoggedin: "",
        online: false,


        init: function() {
            page.events();
            page.getQuote();


        },

        styling: function() {

        },

        events: function() {
            $('form').on('click', '.mainLogIn', function(e) {
                e.preventDefault();
                var user = $(this).siblings(
                    'input[name="username"]').val();
                var pwd = $(this).siblings(
                    'input[name="password"]').val();
                $.ajax({
                    url: "/login",
                    method: 'POST',
                    data: {
                        username: user,
                        password: pwd
                    },
                    success: function(data) {
                        $('.pagecontent').removeClass(
                            'hide');
                        $('.login').addClass(
                            'hide');
                        page.loggedIn(user);
                        $(
                            'input[name="username"]'
                        ).val("");
                        $(
                            'input[name="password"]'
                        ).val("");
                        page.getWorkOut();
                        page.getNote();
                    },
                    failure: function(data) {
                        console.log(
                            "running but not working"
                        );
                    }

                });
            });

            $('form').on('click','.notesubmit', function(e){
             e.preventDefault();
             var theNote = $(this).siblings('input[name="thoughts"]').val();
             $.ajax({
               url:'/create-note',
               method:'POST',
               data: {note: theNote},
               success:function(el){
                 console.log("Success: "+ el);
                 $('input[name="thoughts"]').val("");
                 page.getNote();
               },
               failure:function(){
                 console.log("didn't work");
               },
             });
           });

           $('.noteable').on('click','.deleteNote', function(e){
               e.preventDefault();
               var deleteItem = $(this);
               var noteId = deleteItem.closest('div').data('index');
               $.ajax({
                 url:"/delete-note",
                 method:'POST',
                 data:{noteId: noteId},
                 success:function(){
                   deleteItem.closest('div').remove();
                 },
                 failure:function(){
                   console.log("didn't work");
                 },

               });
           });

               $('nav').on('click',".not", function(event){
                 event.preventDefault();
                 $('.workout').addClass('hide');
                 $('.notes').removeClass('hide');
                 $('.noteBG').css("background-color","#F7840F");
                 $('.workBG').css("background-color","#A2AB2A");
                  page.getNote();
               });

               $('nav').on('click',".work", function(event){
                 event.preventDefault();
                 $('.notes').addClass('hide');
                 $('.workout').removeClass('hide');
                 $('.pagecontent').removeClass('hide');
                 $('.workBG').css("background-color","#F7840F");
                 $('.noteBG').css("background-color","#A2AB2A");
                 page.getWorkOut();
               });



        },
        loggedIn: function(data) {
            page.userLoggedin = data;
            page.online = true;
            window.addEventListener('beforeunload', function() {
                page.userLoggedin = "";
                online = false;
            });

        },
        loggedOut: function(data) {
            page.userLoggedin = "";
            online = false;
        },

        getTemplate: function(tmpl) {
            return _.template(Ourtemplates[tmpl]);
        },

        loadTemplate: function($loc, el, tmpl) {
            var ourhtml = "";
            var thistmple = page.getTemplate(tmpl);
            _.each(el, function(curel) {
                ourhtml += thistmple(curel);
            });
            $loc.html(ourhtml);
        },

        getWorkOut: function() {
            $.ajax({
                url: "/randomWorkout",
                method: 'GET',
                success: function(data) {
                    var datArr = [];
                    datArr.push(JSON.parse(data));
                    page.loadTemplate($('.todaysWorkout'), datArr, 'workingOut');
                },
                failure: function() {
                    console.log("this didn't work");
                },
            });
        },

        getNote: function(){
        $.ajax({
          method:'GET',
          url:'/notes',
          success: function(data){
            var newDat = (JSON.parse(data));
            page.loadNoteTemp($('.noteable'), newDat, 'noteTemp');
          },
        });
      },

          loadNoteTemp: function($loc, el, tmpl){
            var ourhtml = "";
            var thistmple = page.getTemplate(tmpl);
              $loc.html("");
            _.each(el, function(curel) {
                ourhtml = thistmple(curel);
                $loc.prepend(ourhtml);
            });

        },


        getQuote: function(){
             $.ajax({
               method:'GET',
               url:'/random-quote',
               success: function(data){
                 var quoteArr = [];
                 var firstDat = JSON.parse(data);
                 quoteArr.push(JSON.parse(firstDat));
                 _.each(quoteArr, function(el){
                   $('.quote').html("<h2 class="+'quoteHs'+">"+ el.quote +"</h2>");
                 });
               },
             });
             },






    };










})();
