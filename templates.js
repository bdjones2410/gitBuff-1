
var Ourtemplates ={

  workingOut:[
    '<ul id="listWorkout">',
    '<li class="WorkItem"><h3 class="worktype">ARMS</h3>   <%=armWorkout.arm%></li>',
    '<li class="WorkItem"><h3 class="worktype">LEGS</h3>   <%=legWorkout.leg%></li>',
    '<li class="WorkItem"><h3 class="worktype">CORE</h3>   <%=coreWorkout.core%></li>',
    '<li class="WorkItem"><h3 class="worktype">CARDIO</h3>   <%=cardioWorkout.cardio%></li>',
    '</ul>',
    ].join(""),


    noteTemp:[
      '<div class="noteItem" data-index=<%=id%>>',
      '<span><%=noteDate.month%> <%=noteDate.dayOfMonth%></span>',
      '<button type="delete" name="button" class="btn btn-xs deleteNote">X</button>',
      '<p><%=text%></p>',
      '</div>',
    ].join(""),


};
