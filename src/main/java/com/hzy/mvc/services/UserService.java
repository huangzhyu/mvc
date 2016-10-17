package com.hzy.mvc.services;

import com.hzy.mvc.model.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;

/**
 * A sample user service to power the EXTJS application
 */
@Controller
//@RequestMapping("/1") // a version # for this services
public class UserService
{
    // todo: demo only, don't use this Collection
    // For the demo we'll hard code a few users. However, for a real application this will
    // come from a db, most likely.
    private static Map<String, User> users = new HashMap<String, User>();
    static
    {
        User u1 = new User();
        u1.setId(UUID.randomUUID().toString());
        u1.setName("Ed");
        u1.setEmail("ed@sencha.com");
        users.put(u1.getId(), u1);

        User u2 = new User();
        u2.setId(UUID.randomUUID().toString());
        u2.setName("Tommy");
        u2.setEmail("tommy@sencha.com");
        users.put(u2.getId(), u2);
    }

    /**
     * http://127.0.0.1:8080/mvc/user/add?name=1&email=2&id=3,以这种方式结合@RequestBody传递参数是不行的,httpMediaTypeNotSupportedException
     * @return
     */
    @RequestMapping("user/add")
    @ResponseBody
    public User add(@RequestBody User user){
        User u = new User(user);
        return u;
    }

    /**
     * http://127.0.0.1:8080/mvc/user/add3?name=1&email=2&id=3,以这种方式结合@RequestParam传递参数是不行的,missingServletRequestParameterException(Required User parameter 'user' is not present)
     * @param user
     * @return
     */
    @RequestMapping("user/add3")
    @ResponseBody
    public User add3(@RequestParam User user){
        User u = new User(user);
        return u;
    }

    /**
     * http://127.0.0.1:8080/mvc/user/add2?name=1&email=2&id=3,可以正常绑定参数
     * @param user
     * @return
     */
    @RequestMapping("user/add2")
    @ResponseBody
    public User add2( User user){
        User u = new User(user);
        return u;
    }


    /**
     * Authenticate a user
     *
     * @return the authenticated user
     */
    @RequestMapping("/user/authenticate")
    public User authenticateUser( HttpServletRequest request, HttpServletResponse response
            , @RequestParam("email") String email
            , @RequestParam("password") String password
            , @RequestParam(value = "salt", required = false) String salt )
    {
        //TODO replace this with your real authentication code here.

        // return a new user object for the "authenticated" user
        User user = new User();
        user.setId("1");
        user.setName(email.split("@")[0]);
        user.setEmail(email);
        return user;
    }


    /**
     * return a list of users
     */
    @RequestMapping("/user/list")
    public Collection<User> listUsers( HttpServletRequest request, HttpServletResponse response, Principal principal )
    {
        //TODO replace this with your real code here.
        return users.values();
    }


    /**
     * Update a user in the system
     */
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public Map updateUser( HttpServletRequest request, HttpServletResponse response, Principal principal
            , @RequestBody String json ) throws Exception
    {
        //TODO replace this with your real code here.
        Collection<User> parsedUsers = parseUserJson(json);

        // Update all of the users (client is sending us array of users in json)
        if ( parsedUsers != null )
        {
            for (User parsedUser : parsedUsers)
            {
                User localUser = users.get(parsedUser.getId());
                if ( localUser == null )
                {
                    throw new RuntimeException("Invalid User");
                }

                // save changes to local user
                localUser.setName(parsedUser.getName());
                localUser.setEmail(parsedUser.getEmail());
            }
        }


        Map results = new HashMap();
        results.put("succes", true);
        return results;
    }


    /**
     * Parse an json packet of user(s)
     */
    private Collection<User> parseUserJson( String json ) throws Exception
    {
        try
        {
            if ( json.startsWith("[") && json.endsWith("]") )
            {
                // array of users
                ObjectMapper mapper = new ObjectMapper();
                TypeReference ref = new TypeReference<Collection<User>>(){};
                Collection<User> user = (Collection<User>) mapper.readValue(json, ref);
                return user;
            }
            else
            {
                // Single object
                ObjectMapper mapper = new ObjectMapper();
                Collection<User> users = new ArrayList<User>();
                users.add( (User) mapper.readValue(json, User.class) );
                return users;
            }
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Invalid USER Json");
        }
    }
}
