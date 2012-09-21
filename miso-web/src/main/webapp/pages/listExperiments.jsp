<%--
  ~ Copyright (c) 2012. The Genome Analysis Centre, Norwich, UK
  ~ MISO project contacts: Robert Davey, Mario Caccamo @ TGAC
  ~ **********************************************************************
  ~
  ~ This file is part of MISO.
  ~
  ~ MISO is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ MISO is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with MISO.  If not, see <http://www.gnu.org/licenses/>.
  ~
  ~ **********************************************************************
  --%>

<%--
  Created by IntelliJ IDEA.
  User: davey
  Date: 16-Feb-2010
  Time: 08:51:03
--%>
<%@ include file="../header.jsp" %>

<%--<script type="text/javascript" src="<c:url value='/scripts/jquery/js/jquery.popup.js'/>"></script>--%>
<div id="maincontent">
    <div id="contentcolumn">
        <h1>
            <div id="totalCount">
            </div></h1>

        <form id="filter-form">Filter: <input name="filter" id="filter" value="" maxlength="30" size="30" type="text">
        </form>
        <br/>
        <table class="list" id="table">
            <thead>
            <tr>
                <%--<sec:authorize access="hasRole('ROLE_ADMIN')">--%>
                    <%--<th>ID</th>--%>
                <%--</sec:authorize>--%>
                <th>Experiment Name</th>
                <th>Experiment Alias</th>
                <th>Description</th>
                <th>Platform</th>
                <th class="fit">Edit</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${experiments}" var="experiment">
                <tr onMouseOver="this.className='highlightrow'" onMouseOut="this.className='normalrow'">
                    <%--<sec:authorize access="hasRole('ROLE_ADMIN')">--%>
                        <%--<td>--%>
                                <%--${experiment.experimentId}--%>
                        <%--</td>--%>
                    <%--</sec:authorize>--%>
                    <td>
                       <b>${experiment.name}</b>
                        <%--<div class="bubbleInfo">--%>
                            <%--<div class="trigger"><b>${experiment.name}</b>--%>
                                <%--<span class="fg-button ui-icon ui-icon-info"></span></div>--%>

                            <%--<div class="popup">--%>
                                <%--<ul class="popup-bullets">--%>
                                   <%--<!-- <li><b>Description:</b> ${experiment.description}<br/></li>-->--%>
                                    <%--<li><b>Owner:</b> ${experiment.securityProfile.owner.fullName}</li>--%>
                                <%--</ul>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    </td>
                    <td>${experiment.alias}</td>
                    <td>${experiment.description}</td>
                    <td>${experiment.platform.platformType.key} - ${experiment.platform.instrumentModel}</td>
                    <td class="misoicon"
                        onclick="window.location.href='<c:url value="/miso/experiment/${experiment.id}"/>'"><span class="ui-icon ui-icon-pencil"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <script type="text/javascript">
            jQuery(document).ready(function() {
                writeTotalNo();
                jQuery("#table").tablesorter({
                    headers: {
                        4: {
                            sorter: false
                        }
                    }
                });
            });

            jQuery(function() {
                var theTable = jQuery("#table");

                jQuery("#filter").keyup(function() {
                    jQuery.uiTableFilter(theTable, this.value);
                    writeTotalNo();
                });

                jQuery('#filter-form').submit(function() {
                    theTable.find("tbody > tr:visible > td:eq(1)").mousedown();
                    return false;
                }).focus(); //Give focus to input field
            });

            function writeTotalNo() {
                jQuery('#totalCount').html(jQuery('#table>tbody>tr:visible').length.toString() + " Experiments");
            }
        </script>
    </div>
</div>
<%@ include file="adminsub.jsp" %>

<%@ include file="../footer.jsp" %>