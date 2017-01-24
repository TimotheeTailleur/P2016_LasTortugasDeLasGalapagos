import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.code.stackexchange.schema.Question;

import fr.tse.info4.project.controller.UserFactory;
import fr.tse.info4.project.model.user.Bob;

public class as {
	public static void main(String[] args) {
		Bob bob = (new UserFactory()).newBob().get();
		List<Question> list = bob.findSimilarQuestions("how to use");
		for (int i =0; i<list.size(); i++){
			System.out.println(StringEscapeUtils.unescapeHtml(list.get(i).getTitle()));
			System.out.println(list.get(i).getTitle());
		}
	}
}