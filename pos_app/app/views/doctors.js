'use client';

import React, { useState } from "react";
import { useEffect } from "react";


var State;
var setLoading;
var setData;
var setPage;
var setMax;
var setUrl;

var isLoading;
var list;
var page;
var maxPages;
var startedFetch = false;
var url;

var baseUrl = "http://localhost:8080/api/medical_office/physicians/?page=";


var specRef = React.createRef();
var docNameRef = React.createRef();
var itemsRef = React.createRef();

export function Doctors({setState})
{
    [isLoading, setLoading] = useState(true);
    [list, setData] = useState(null);
    [page, setPage] = useState(0);
    [maxPages, setMax] = useState(0);
    [url, setUrl] = useState(baseUrl + "0");
    State = setState;

    useEffect(() => 
    {
        fetch(url, 
        {
            method: "GET",
            headers: {'Authorization':'Bearer ' + localStorage.getItem("token")}
        })
        .then((res) => res.json())
        .then((data) => 
        {
          setData(data);
          setLoading(false);
          setPage(data.page.number);
          setMax(data.page.totalPages - 1);
          startedFetch = false;
        });
    }, [url]);

    if(isLoading) return (<div><h2>Loading...</h2></div>);
    isLoading = false;

    if(!list._embedded)    
    {
        return (
            <div>
                <div className="header">
                    <button>Doctors</button>
                </div>
                <br/>
    
                <label htmlFor="spec">Specialization:</label>
                <select name="spec" onChange={filter} ref={specRef}>
                    <option value="a">a</option>
                    <option value="b">b</option>
                </select>
                <label htmlFor="docName">Name starts with:</label>
                <input name="docName" type="text" onChange={filter} ref={docNameRef}/>
                <label htmlFor="items">Items per page:</label>
                <input name="items" type="number" min={1} max={maxPages} onChange={filter} ref={itemsRef}/>
    
                <table>
                    <tr>
                        <th>Lastname</th>
                        <th>Firstname</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Specialization</th>
                    </tr>
                </table>
            </div>
        );
    }

    return (
        <div>
            <div className="header">
                <button>Doctors</button>
            </div>
            <br/>

            <label htmlFor="spec">Specialization:</label>
            <select name="spec" onChange={filter} ref={specRef}>
                <option value="none"></option>
                <option value="a">a</option>
                <option value="b">b</option>
            </select>
            <label htmlFor="docName">Name starts with:</label>
            <input name="docName" type="text" onChange={filter} ref={docNameRef}/>
            <label htmlFor="items">Items per page:</label>
            <input name="items" type="number" min={1} max={maxPages} onChange={filter} ref={itemsRef}/>

            <table>
                <tr>
                    <th>Lastname</th>
                    <th>Firstname</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Specialization</th>
                </tr>
                {list._embedded.doctorList.map((doctor) => (
                    <tr>
                        <td>{doctor.lastname}</td>
                        <td>{doctor.firstname}</td>
                        <td>{doctor.email}</td>
                        <td>{doctor.phone}</td>
                        <td>{doctor.specialization}</td>
                    </tr>
                    ))}
            </table>
            <div className="centered">
                <button onClick={firstPage}>First</button>
                <button onClick={previousPage}>Previous</button>
                <label>{list.page.number}</label>
                <button onClick={nextPage}>Next</button>
                <button onClick={lastPage}>Last</button>
            </div>
        </div>
    );
}

function getData()
{
    if(!startedFetch)
    {
        startedFetch = true;
        fetch("http://localhost:8080/api/medical_office/physicians/?page=" + page, 
        {
            method: "GET",
            headers: {'Authorization':'Bearer ' + localStorage.getItem("token")}
        }).then((res) => res.json(), [])
          .then((data) => 
          {
            setData(data);
            setLoading(false);
            setPage(data.page.number);
            setMax(data.page.totalPages - 1);
            startedFetch = false;
          });
    }
}

function nextPage()
{
    if(page < (maxPages))
    {
        setUrl(list._links.next.href);
    }
}

function previousPage()
{
    if(page > 0)
    {
        setUrl(list._links.prev.href); 
    }
}

function firstPage()
{
    setUrl(list._links.first.href);
}

function lastPage()
{
    setUrl(list._links.last.href);
}

function filter()
{
    var query = page;
    if(specRef.current.value != "none")
        quer = query + "&specialization=" + specRef.current.value;

    if(docNameRef.current.value != "")
        query = query + "&name=" + docNameRef.current.value;

    if(itemsRef.current.value != undefined)
        query = query + "&items=" + itemsRef.current.value;

    setUrl(baseUrl + query);
}